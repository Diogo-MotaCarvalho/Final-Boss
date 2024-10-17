package com.finalboss.useCases;

import com.finalboss.domain.Market;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.YellowEvent;
import com.finalboss.mapper.YellowEventMapper;
import com.finalboss.port.EventPublisher;
import com.finalboss.repository.Repository;
import com.finalboss.repository.YellowEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class EventHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);
    private final YellowEventRepository repo;
    private final YellowEventMapper yellowEventMapper;
    private final Publisher publisher;
    private final Repository repository;


    public EventHandler(YellowEventRepository repo, YellowEventMapper yellowEventMapper, EventPublisher eventPublisher, Repository repository) {
        this.repo = repo;
        this.yellowEventMapper = yellowEventMapper;
        this.publisher = eventPublisher;
        this.repository = repository;
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
     * Adds a Market to a YellowEvent on the repository from a given MarketUpdate
     * and publishes it to the designated kafka topic.
     * If the YellowEvent does not exist a new one is created.
     *
     * @param update - must not be null.
     * @throws IllegalArgumentException – in case the given update is null.
     */
    public YellowEvent addYellowEventToRepository(MarketUpdate update) {
        log.info("operation=addYellowEventToRepository, message='trying to add event to repository', update='{}'", update);
        // Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = repo.findById(yellowEventUpdate.id());

        if (event.isPresent()) {
            // If the Event exists then verify if it has a Market with the id received in the email.
            return repository.addMarketToEvent(event.get(), yellowEventUpdate.markets().getFirst());
        } else {
            // If the Event does not exist then it must be added with the market received.
            return repository.createNewEvent(yellowEventUpdate);
        }
    }

    /**
     * Modifies a Market and its Selections on a YellowEvent to the repository from a given MarketUpdate
     * and publishes the Event to the designated kafka topic.
     *
     * @param update - must not be null.
     * @throws IllegalArgumentException – in case the given update is null.
     */
    private YellowEvent modifyYellowEventInRepository(MarketUpdate update) {
        log.info("operation=modifyYellowEventInRepository, message='trying to modify an event's market', update='{}'", update);
        //Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = repo.findById(yellowEventUpdate.id());
        if (event.isPresent()) {
            //If it exists then verify if it has a Market with the id received in the email.
            repository.updateMarketAtEvent(event.get(), yellowEventUpdate.markets().getFirst());
        } else {
            log.info("operation=modifyYellowEventInRepository, message='event does not exist', update='{}'", update);
            return null;
            //If it doesn't exist you can ignore that email.
        }
        return null;
    }

    /**
     * Removes a Market from a YellowEvent to the repository from a given MarketUpdate
     * and publishes it to the designated kafka topic.
     *
     * @param update - must not be null.
     * @throws IllegalArgumentException – in case the given update is null.
     */
    private YellowEvent removeMarketFromEventInRepository(MarketUpdate update) {
        log.info("operation=removeMarketFromEventInRepository, message='trying to remove a market from an event', update='{}'", update);

        YellowEvent yellowEventUpdate = yellowEventMapper.buildYellowEvent(update);
        Optional<YellowEvent> event = repo.findById(yellowEventUpdate.id());
        YellowEvent finalEvent;
        //Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        if (event.isPresent()) {
            //If it exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.map(YellowEvent::markets).orElse(Collections.emptyList());
            for (Market market : markets) {
                log.info("operation=removeMarketFromEventInRepository, message='trying to remove a market', update='{}'", update);
                //If it exists then you should remove the Market from that Event.
                if (market.id().equals(update.id())) {
                    ArrayList<Market> newMarkets = new ArrayList<>(yellowEventUpdate.markets());
                    newMarkets.remove(market);
                    YellowEvent eventToPublish = saveWithTryCatch(new YellowEvent(
                            event.get().id(),
                            event.get().name(),
                            event.get().date(),
                            newMarkets
                    ));
                    log.info("operation=removeMarketFromEventInRepository, message='successfully removed a market', update='{}'", update);
                    publishWithLogs(eventToPublish);
                    return event.get();
                }
            }
        } else {
            log.info("operation=removeMarketFromEventInRepository, message='event does not exist', update='{}'", update);
        }
        return null;
    }

    private YellowEvent saveWithTryCatch(YellowEvent yellowEvent) {
        try {
            return repo.save(yellowEvent);
        } catch (Exception e) {
            log.error("operation=addYellowEventToRepository, message='failed to save yellow event', yellowEvent='{}'", yellowEvent, e);
            throw e;
        }
    }

    private void publishWithLogs(YellowEvent yellowEvent) {
        log.info("operation=addYellowEventToRepository, message='trying to publish event', update='{}'", yellowEvent);
        publisher.publish(yellowEvent);
        log.info("operation=addYellowEventToRepository, message='event successfully published', update='{}'", yellowEvent);
    }
}
