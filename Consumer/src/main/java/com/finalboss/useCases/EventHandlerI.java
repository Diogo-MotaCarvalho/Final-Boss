package com.finalboss.useCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalboss.domain.YellowEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface EventHandlerI {
    void readOperation(ConsumerRecord<String, String> record) throws JsonProcessingException;

    YellowEvent buildYellowEvent(ConsumerRecord<String,String> record) throws JsonProcessingException;

}
