package com.finalboss.useCases;

import com.finalboss.domain.Market;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.YellowEvent;
import com.finalboss.mapper.YellowEventMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class EventHandler implements EventHandlerI {

    private static final Logger log = Logger.getLogger(EventHandler.class.getName());

    private final YellowEventRepositoryI yellowEventRepositoryI;

    private final YellowEventMapper yellowEventMapper;

    public EventHandler(YellowEventRepositoryI yellowEventRepositoryI, YellowEventMapper yellowEventMapper) {
        this.yellowEventRepositoryI = yellowEventRepositoryI;
        this.yellowEventMapper = yellowEventMapper;
    }

    @Override
    public void readOperation(MarketUpdate update) {
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
    private void addYellowEventToRepository(MarketUpdate update) {
        // Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = yellowEventRepositoryI.findById(yellowEventUpdate.id());
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
                yellowEventRepositoryI.save(event.get());
            }
        } else {
            // If the Event does not exist then it must be added with the market received.
            yellowEventRepositoryI.save(yellowEventUpdate);
        }
    }

    /**
     * Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
     * If it exists then verify if it has a Market with the id received in the email.
     * If it already exists then you should update its name and selections with the ones received in the email.
     * If it doesn't exist you can ignore that email.
     */
    private void modifyYellowEventInRepository(MarketUpdate update) {
        //Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        if (yellowEventRepositoryI.existsById(yellowEventUpdate.id())) {
            //If it exists then verify if it has a Market with the id received in the email.
            Optional<YellowEvent> event = yellowEventRepositoryI.findById(yellowEventUpdate.id());

            List<Market> markets = event.map(YellowEvent::markets).orElse(Collections.emptyList());

            for (Market market : markets) {
                //If it already exists then you should update its name and selections with the ones received in the email.
                if (market.id().equals(update.id())) {
                    markets.remove(market);
                    markets.add(yellowEventUpdate.markets().getFirst());
                    yellowEventRepositoryI.save(new YellowEvent(
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

    private void removeYellowEventFromRepository(MarketUpdate update) {

    }

}
