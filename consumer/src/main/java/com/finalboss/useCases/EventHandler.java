package com.finalboss.useCases;

import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.YellowEvent;
import com.finalboss.mapper.YellowEventMapper;
import com.finalboss.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventHandler implements Handler {

    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);
    private final YellowEventMapper yellowEventMapper;
    private final Repository repository;

    public EventHandler(Repository repository, YellowEventMapper yellowEventMapper) {
        this.yellowEventMapper = yellowEventMapper;
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
        return repository.addYellowEventToRepository(yellowEventUpdate);
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
        return repository.modifyYellowEventInRepository(yellowEventUpdate);
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
        return repository.removeYellowEventFromRepository(yellowEventUpdate);
    }

}
