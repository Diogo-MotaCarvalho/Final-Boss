package com.finalboss.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalboss.domain.Market;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.YellowEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YellowEventMapper {

    public YellowEvent buildYellowEvent(MarketUpdate update){
        return new YellowEvent(update.event().id(),
                update.event().name(),
                update.event().date(),
                List.of(buildMarket(update))
                );
    }

    public Market buildMarket(MarketUpdate update) {
        return new Market(
                update.id(),
                update.name(),
                update.selections()
        );
    }
}
