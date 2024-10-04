package com.finalboss.useCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.YellowEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventHandlerI {
    void readOperation(MarketUpdate update) throws JsonProcessingException;
}
