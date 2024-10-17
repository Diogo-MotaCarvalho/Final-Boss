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

    private Optional<YellowEvent> findById(String id) {
        return repo.findById(id);
    }

    public YellowEvent addMarketToEvent(YellowEvent eventAtRepo, Market market) {
        if (!eventHasMarket(eventAtRepo, market.id())) {
            List<Market> newMarkets = new ArrayList<>(List.of(market));
            newMarkets.addAll(eventAtRepo.markets());
            YellowEvent finalEvent = new YellowEvent(
                    eventAtRepo.id(),
                    eventAtRepo.name(),
                    eventAtRepo.date(),
                    newMarkets
            );
            log.info("operation=addYellowEventToRepository, message='trying to a new market to existing event', market='{}'", market);
            saveWithTryCatch(finalEvent);
            log.info("operation=addYellowEventToRepository, message='marked successfully added', market='{}'", market);
            return finalEvent;
        } else {
            log.info("operation=modifyYellowEventInRepository, message='market does not exist in event', market='{}'", market);
            return null;
        }
    }

    public YellowEvent updateMarketAtEvent(YellowEvent eventAtRepo, Market market) {
        if (eventHasMarket(eventAtRepo, market.id())) {
            List<Market> markets = eventAtRepo.markets();
            for (Market marketAtRepo : markets) {
                log.info("operation=modifyYellowEventInRepository, message='trying to modify a market', market='{}'", market);
                //If it already exists then you should update its name and selections with the ones received in the email.
                if (marketAtRepo.id().equals(market.id())) {
                    List<Market> newMarkets = new ArrayList<>(List.of(market));
                    YellowEvent eventToPublish = saveWithTryCatch(new YellowEvent(
                            eventAtRepo.id(),
                            market.name(),
                            eventAtRepo.date(),
                            newMarkets
                    ));
                    log.info("operation=modifyYellowEventInRepository, message='market successfully modified', market='{}'", market);
                    publishWithLogs(eventToPublish);
                    return eventToPublish;
                }
            }
        }else{
            log.info("operation=modifyYellowEventInRepository, message='market does not exist in event', market='{}'", market);
            return null;
        }
        return null;
    }

    public YellowEvent createNewEvent(YellowEvent event) {
        log.info("operation=addYellowEventToRepository, message='trying to create a new event', event='{}'", event);
        saveWithTryCatch(event);
        log.info("operation=addYellowEventToRepository, message='new event successfully created', event='{}'", event);
        publishWithLogs(event);
        return event;
    }

    private boolean eventHasMarket(YellowEvent event, String marketId) {
        List<Market> markets = event.markets();
        boolean hasMarket = false;
        for (Market market : markets) {
            if (market.id().equals(marketId)) {
                hasMarket = true;
                log.info("operation=eventHasMarket, message='market already exists in event', market='{}'", market);
                return hasMarket;
            }
        }
        return hasMarket;
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
