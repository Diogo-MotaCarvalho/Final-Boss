package com.finalboss.service;

import com.finalboss.domain.MarketUpdate;
import com.finalboss.useCases.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);
    private final EventHandler eventHandler;

    public Consumer(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @KafkaListener(topics = {"${spring.kafka.consumer.topic}"})
    public void onMessage(@Payload List<MarketUpdate> message) {
        log.info("operation=onMessage, message='reading a message', message='{}'", message);
        eventHandler.readOperation(message.getFirst());
        log.info("operation=onMessage, message='operation successful', message='{}'", message);

    }
}
