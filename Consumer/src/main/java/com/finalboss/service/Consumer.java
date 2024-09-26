package com.finalboss.service;

import com.finalboss.http.ProducerEventController;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(ProducerEventController.class);

    @KafkaListener(topics={"demo"})
    public void onMessage(ConsumerRecord<String,?> record) {
        log.info("operation=read, message='reading a message', message='{}'", record);
    }

}
