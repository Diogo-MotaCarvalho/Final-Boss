package com.finalboss.useCases;

import com.finalboss.domain.YellowEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaConsumer implements Consumer {

    private static final Logger log = Logger.getLogger(KafkaConsumer.class.getName());

    private final KafkaTemplate<String,YellowEvent> kafkaTemplate;

    public KafkaConsumer(KafkaTemplate<String, YellowEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public YellowEvent buildYellowEvent() {
        return null;
    }

    @Override
    public void readData() {

    }
}
