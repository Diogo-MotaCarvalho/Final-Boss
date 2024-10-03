package com.finalboss.useCases;

import com.finalboss.domain.MarketUpdate;
import com.finalboss.http.ProducerEventController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaPublisher implements Publisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPublisher.class);
    private final KafkaTemplate<String, MarketUpdate> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    public KafkaPublisher(KafkaTemplate<String, MarketUpdate> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(MarketUpdate message) {
        log.info("operation=send, message='sending a message', message='{}'", message);
        kafkaTemplate.send(topic, message);
        log.info("operation=send, message='message sent', message='{}'", message);
    }
}
