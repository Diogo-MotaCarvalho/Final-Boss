package com.finalboss.http;


import com.finalboss.useCases.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ConsumerEventController {

    private static final Logger log = LoggerFactory.getLogger(ProducerEventController.class);
    private final Consumer consumer;

    public ConsumerEventController(Consumer consumer) {
        this.consumer = consumer;
    }

    @PostMapping("/marketUpdate")
    public String readData(@RequestBody String message) {
        log.info("operation=readData",message='');
    }
}
