package com.finalboss.useCases;

import com.finalboss.domain.Market;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.YellowEvent;
import com.finalboss.mapper.YellowEventMapper;
import com.finalboss.repository.YellowEventRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

@Component
public class EventHandler implements EventHandlerI {

    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);
    private final YellowEventRepository repo;
    private final YellowEventMapper yellowEventMapper;

    public EventHandler(YellowEventRepository repo, YellowEventMapper yellowEventMapper) {
        this.repo = repo;
        this.yellowEventMapper = yellowEventMapper;
    }
    @Override
    public void readOperation(MarketUpdate update) {
        switch (update.operation()) {
            case Operation.ADD -> addYellowEventToRepository(update);
            case Operation.MODIFY -> modifyYellowEventInRepository(update);
            case Operation.DELETE -> removeMarketFromEventInRepository(update);
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
        log.info("operation=addYellowEventToRepository, message='trying to add event to repository', update='{}'", update);
        // Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = repo.findById(yellowEventUpdate.id());
        if (event.isPresent()) {
            // If the Event exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.get().markets();
            boolean hasMarket = false;
            for (Market market : markets) {
                //If already exists then you can ignore that email.
                if (market.id().equals(update.id())) {
                    hasMarket = true;
                    log.info("operation=addYellowEventToRepository, message='market already exists in event', update='{}'", update);
                    break;
                }
            }
            //If not, add that market to the list of markets of that Event.
            if (!hasMarket) {
                event.get().markets().add(yellowEventUpdate.markets().getFirst());
                log.info("operation=addYellowEventToRepository, message='trying to a new market to existing event', update='{}'", update);
                repo.save(event.get());
                log.info("operation=addYellowEventToRepository, message='marked successfully added', update='{}'", update);
            }
        } else {
            // If the Event does not exist then it must be added with the market received.
            log.info("operation=addYellowEventToRepository, message='trying to a new event', update='{}'", update);
            repo.save(yellowEventUpdate);
            log.info("operation=addYellowEventToRepository, message='new event successfully added', update='{}'", update);
        }
    }
    /**
     * Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
     * If it exists then verify if it has a Market with the id received in the email.
     * If it already exists then you should update its name and selections with the ones received in the email.
     * If it doesn't exist you can ignore that email.
     */
    private void modifyYellowEventInRepository(MarketUpdate update) {
        log.info("operation=modifyYellowEventInRepository, message='trying to modify an event's market', update='{}'", update);
        //Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = repo.findById(yellowEventUpdate.id());
        if (event.isPresent()) {
            //If it exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.map(YellowEvent::markets).orElse(Collections.emptyList());
            for (Market market : markets) {
                log.info("operation=modifyYellowEventInRepository, message='trying to modify a market', update='{}'", update);
                //If it already exists then you should update its name and selections with the ones received in the email.
                if (market.id().equals(update.id())) {
                    markets.remove(market);
                    markets.add(yellowEventUpdate.markets().getFirst());
                    repo.save(new YellowEvent(
                            event.get().id(),
                            yellowEventUpdate.name(),
                            event.get().date(),
                            markets
                    ));
                    log.info("operation=modifyYellowEventInRepository, message='market successfully modified', update='{}'", update);
                }
            }
            log.info("operation=modifyYellowEventInRepository, message='market does not exist in event', update='{}'", update);
        }else{
            log.info("operation=modifyYellowEventInRepository, message='event does not exist', update='{}'", update);
            //If it doesn't exist you can ignore that email.
        }

    }
    /**
     * Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
     * If it exists then verify if it has a Market with the id received in the email.
     * If it exists then you should remove the Market from that Event.
     * If that Market does not exist in the specified Event then you can ignore that email.
     */
    private void removeMarketFromEventInRepository(MarketUpdate update) {
        log.info("operation=removeMarketFromEventInRepository, message='trying to remove a market from an event', update='{}'", update);
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = repo.findById(yellowEventUpdate.id());
        //Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        if (event.isPresent()) {
            //If it exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.map(YellowEvent::markets).orElse(Collections.emptyList());
            for (Market market : markets) {
                log.info("operation=removeMarketFromEventInRepository, message='trying to remove a market', update='{}'", update);
                //If it exists then you should remove the Market from that Event.
                if (market.id().equals(update.id())) {
                    markets.remove(market);
                    repo.save(event.get());
                    log.info("operation=removeMarketFromEventInRepository, message='market successfully removed', update='{}'", update);
                }
            }
            log.info("operation=removeMarketFromEventInRepository, message='market does not exist in event', update='{}'", update);
        }else{
            log.info("operation=removeMarketFromEventInRepository, message='event does not exist', update='{}'", update);
        }
    }
}
