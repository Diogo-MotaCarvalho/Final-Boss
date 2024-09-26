package com.finalboss.http;

import com.finalboss.service.Consumer;
import com.finalboss.useCases.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

public class ConsumerEventController {

    private static final Logger log = LoggerFactory.getLogger(ConsumerEventController.class);

    private final Consumer consumer;

    public ConsumerEventController(Consumer consumer){this.consumer = consumer;}

}
