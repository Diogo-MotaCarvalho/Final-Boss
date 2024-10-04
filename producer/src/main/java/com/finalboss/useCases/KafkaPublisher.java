package com.finalboss.useCases;

import com.finalboss.domain.MarketUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPublisher implements Publisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPublisher.class);
    private final KafkaTemplate<String, MarketUpdate> kafkaTemplate;
    private final String topic;

    public KafkaPublisher(@Value("${spring.kafka.producer.topic:demo}") String topic, KafkaTemplate<String, MarketUpdate> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(MarketUpdate message) {
        log.info("operation=send, message='sending a message', message='{}'", message);
        kafkaTemplate.send(topic, message);
        log.info("operation=send, message='message sent', message='{}'", message);
    }

}
