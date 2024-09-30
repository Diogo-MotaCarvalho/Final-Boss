package com.finalboss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finalboss.http.ConsumerEventController;
import com.finalboss.useCases.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(ConsumerEventController.class);

    private final KafkaConsumer kafkaConsumer;

    public Consumer(KafkaConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }



    @KafkaListener(topics={"demo"},groupId = "final-boss-consumer")
    public void onMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.info("operation=reading, message='reading a message', message='{}'", record);
        kafkaConsumer.buildYellowEvent(record);

    }

}
