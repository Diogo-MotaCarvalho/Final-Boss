package com.finalboss.mapper;

import com.finalboss.domain.Market;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.YellowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YellowEventMapper {
    private static final Logger log = LoggerFactory.getLogger(YellowEventMapper.class);

    public YellowEvent buildYellowEvent(MarketUpdate update) {
        log.info("operation=buildYellowEvent, message='building YellowEvent', update='{}'", update);
        if (update == null) throw new IllegalArgumentException("The update cannot be null");

        return new YellowEvent(
                update.event().id(),
                update.event().name(),
                update.event().date(),
                List.of(buildMarket(update
                ))
        );
    }

    public Market buildMarket(MarketUpdate update) {
        log.info("operation=buildYellowEvent, message='building Market', update='{}'", update);
        if (update == null) throw new IllegalArgumentException("The update cannot be null");

        return new Market(
                update.id(),
                update.name(),
                update.selections()
        );

    }
}
