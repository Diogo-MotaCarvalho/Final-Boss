package com.finalboss.useCases;

import com.finalboss.domain.YellowEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface Consumer {
    YellowEvent buildYellowEvent(ConsumerRecord<String,?> record);
}
