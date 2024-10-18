package com.finalboss.repository;

import com.finalboss.domain.Market;
import com.finalboss.domain.YellowEvent;
import com.finalboss.useCases.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class Repository {
    private static final Logger log = LoggerFactory.getLogger(Repository.class);

    private final YellowEventRepository repo;
    private final Publisher publisher;

    public Repository(YellowEventRepository repo, Publisher publisher) {
        this.repo = repo;
        this.publisher = publisher;
    }

    public YellowEvent addYellowEventToRepository(YellowEvent yellowEvent) {
        Optional<YellowEvent> event = repo.findById(yellowEvent.id());
        if (event.isPresent()) {
            // If the Event exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.get().markets();
            // TODO learn streams (check example on commit "TODO"
            boolean hasMarket = false;
            for (Market market : markets) {
                //If already exists then you can ignore that email.
                if (market.id().equals(yellowEvent.markets().getFirst().id())) {
                    hasMarket = true;
                    log.info("operation=addYellowEventToRepository, message='market already exists in event', market='{}'", market);
                    break;
                }
            }
            //If not, add that market to the list of markets of that Event.
            if (!hasMarket) {
                List<Market> newMarkets = new ArrayList<>(yellowEvent.markets());
                newMarkets.addAll(event.get().markets());
                YellowEvent finalEvent = new YellowEvent(
                        event.get().id(),
                        event.get().name(),
                        event.get().date(),
                        newMarkets
                );
                log.info("operation=addYellowEventToRepository, message='trying to a new market to existing event', event='{}'", finalEvent);
                saveWithTryCatch(finalEvent);
                log.info("operation=addYellowEventToRepository, message='marked successfully added', event='{}'", finalEvent);
                publishWithLogs(finalEvent);
                return finalEvent;
            }
        } else {
            // If the Event does not exist then it must be added with the market received.
            log.info("operation=addYellowEventToRepository, message='trying to create a new event', event='{}'", yellowEvent);
            saveWithTryCatch(yellowEvent);
            log.info("operation=addYellowEventToRepository, message='new event successfully created', event='{}'", yellowEvent);
            publishWithLogs(yellowEvent);
            return yellowEvent;
        }
        return null;
    }

    public YellowEvent modifyYellowEventInRepository(YellowEvent yellowEvent) {
        Optional<YellowEvent> event = repo.findById(yellowEvent.id());
        if (event.isPresent()) {
            //If it exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.map(YellowEvent::markets).orElse(Collections.emptyList());
            for (Market market : markets) {
                log.info("operation=modifyYellowEventInRepository, message='trying to modify a market', market='{}'", market);
                //If it already exists then you should update its name and selections with the ones received in the email.
                if (market.id().equals(yellowEvent.markets().getFirst().id())) {
                    List<Market> newMarkets = new ArrayList<>(yellowEvent.markets());
                    YellowEvent eventToPublish = saveWithTryCatch(new YellowEvent(
                            event.get().id(),
                            yellowEvent.name(),
                            event.get().date(),
                            newMarkets
                    ));
                    log.info("operation=modifyYellowEventInRepository, message='market successfully modified', newMarkets='{}'", newMarkets);
                    publishWithLogs(eventToPublish);
                    return eventToPublish;
                }
            }
        } else {
            log.info("operation=modifyYellowEventInRepository, message='event does not exist', yellowEvent='{}'", yellowEvent);
            return null;
            //If it doesn't exist you can ignore that email.
        }
        log.info("operation=modifyYellowEventInRepository, message='market does not exist in event', yellowEvent='{}'", yellowEvent);
        return null;
    }

    public YellowEvent removeYellowEventFromRepository(YellowEvent yellowEvent) {
        Optional<YellowEvent> event = repo.findById(yellowEvent.id());
        //Check the Yellow Events to verify if an Event with the ID received in event.id already exists.
        if (event.isPresent()) {
            //If it exists then verify if it has a Market with the id received in the email.
            List<Market> markets = event.map(YellowEvent::markets).orElse(Collections.emptyList());
            for (Market market : markets) {
                log.info("operation=removeMarketFromEventInRepository, message='trying to remove a market', market='{}'", market);
                //If it exists then you should remove the Market from that Event.
                if (market.id().equals(yellowEvent.markets().getFirst().id())) {
                    ArrayList<Market> newMarkets = new ArrayList<>(yellowEvent.markets());
                    newMarkets.remove(market);
                    YellowEvent eventToPublish = saveWithTryCatch(new YellowEvent(
                            event.get().id(),
                            event.get().name(),
                            event.get().date(),
                            newMarkets
                    ));
                    log.info("operation=removeMarketFromEventInRepository, message='successfully removed a market', market='{}'", market);
                    publishWithLogs(eventToPublish);
                    return event.get();
                }
            }
        } else {
            log.info("operation=removeMarketFromEventInRepository, message='event does not exist', yellowEvent='{}'", yellowEvent);
        }
        return null;
    }

    private YellowEvent saveWithTryCatch(YellowEvent yellowEvent) {
        try {
            return repo.save(yellowEvent);
        } catch (Exception e) {
            log.error("operation=addYellowEventToRepository, message='failed to save yellow event', yellowEvent='{}'", yellowEvent, e);
            throw  e;
        }
    }

    private void publishWithLogs(YellowEvent yellowEvent) {
        log.info("operation=addYellowEventToRepository, message='trying to publish event', yellowEvent='{}'", yellowEvent);
        publisher.publish(yellowEvent);
        log.info("operation=addYellowEventToRepository, message='event successfully published', yellowEvent='{}'", yellowEvent);
    }
}
