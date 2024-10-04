package com.finalboss.port;

import com.finalboss.domain.YellowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher implements Publisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private final KafkaTemplate<String, YellowEvent> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    public EventPublisher(KafkaTemplate<String, YellowEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(YellowEvent message) {
        log.info("operation=send, message='publishing event', message='{}'", message);
        kafkaTemplate.send(topic, message); // TODO can we do something with the return of this method? Not a priority but a nice to have.
        log.info("operation=send, message='event published', message='{}'", message);
    }
}

