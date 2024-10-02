package com.finalboss.useCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalboss.domain.Market;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.YellowEvent;
import com.finalboss.mapper.YellowEventMapper;
import com.finalboss.repository.YellowEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class EventHandler implements EventHandlerI {

    private static final Logger log = Logger.getLogger(EventHandler.class.getName());
    private final YellowEventRepository repository;

    private final YellowEventMapper yellowEventMapper;

    public EventHandler(YellowEventRepository repository, YellowEventMapper yellowEventMapper) {
        this.repository = repository;
        this.yellowEventMapper = yellowEventMapper;
    }

    @Override
    public void readOperation(MarketUpdate update) throws JsonProcessingException {
        switch (update.operation()) {
            case Operation.ADD -> addYellowEventToRepository(update);
            case Operation.MODIFY -> modifyYellowEventInRepository(update);
            case Operation.DELETE -> removeYellowEventFromRepository(update);
        }
    }

    /**
     * Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
     * If the Event does not exist then it must be added with the market received.
     * If the Event exists then verify if it has a Market with the id received in the email.
     * If not, add that market to the list of markets of that Event.
     * If already exists then you can ignore that email.
     */
    private void addYellowEventToRepository(MarketUpdate update) throws JsonProcessingException {
        // Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = repository.findById(yellowEventUpdate.id());
        if (event.isPresent()) {
            // If the Event exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.get().markets();
            boolean hasMarket = false;
            for (Market market : markets) {
                //If already exists then you can ignore that email.
                if (market.id().equals(update.id())) {
                    hasMarket = true;
                    break;
                }
            }
            //If not, add that market to the list of markets of that Event.
            if (!hasMarket) {
                event.get().markets().add(yellowEventUpdate.markets().getFirst());
                repository.save(event.get());
            }
        } else {
            // If the Event does not exist then it must be added with the market received.
            repository.save(yellowEventUpdate);
        }
    }

    /**
     * Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
     * If it exists then verify if it has a Market with the id received in the email.
     * If it already exists then you should update its name and selections with the ones received in the email.
     * If it doesn't exist you can ignore that email.
     */
    private void modifyYellowEventInRepository(MarketUpdate update) throws JsonProcessingException {
        //Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        if (repository.existsById(yellowEventUpdate.id())) {
            //If it exists then verify if it has a Market with the id received in the email.
            Optional<YellowEvent> event = repository.findById(yellowEventUpdate.id());

            List<Market> markets = event.get().markets();

            for (Market market : markets) {
                //If it already exists then you should update its name and selections with the ones received in the email.
                if (market.id().equals(update.id())) {
                    markets.remove(market);
                    markets.add(yellowEventUpdate.markets().getFirst());
                    repository.save(new YellowEvent(
                            event.get().id(),
                            event.get().name(),
                            event.get().date(),
                            markets
                    ));
                }
            }
        }
        //If it doesn't exist you can ignore that email.
    }

    private void removeYellowEventFromRepository(MarketUpdate update) throws JsonProcessingException {

    }

}
