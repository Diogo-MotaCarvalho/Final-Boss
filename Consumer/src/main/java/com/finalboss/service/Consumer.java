package com.finalboss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalboss.useCases.EventHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    private final EventHandler eventHandler;

    public Consumer(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }



    @KafkaListener(topics={"demo"},groupId = "final-boss-consumer")
    public void onMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.info("operation=reading, message='reading a message', message='{}'", record);
        eventHandler.buildYellowEvent(record);

    }

}
