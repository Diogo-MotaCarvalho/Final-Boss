package com.finalboss.useCases;

import com.finalboss.domain.YellowEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaConsumer implements Consumer {

    private static final Logger log = Logger.getLogger(KafkaConsumer.class.getName());

    private final KafkaTemplate<String,?> kafkaTemplate;

    public KafkaConsumer(KafkaTemplate<String, ?> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public YellowEvent buildYellowEvent(ConsumerRecord<String,?> record) {
        return null;
    }

}
