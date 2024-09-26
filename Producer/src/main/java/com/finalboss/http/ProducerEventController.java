package com.finalboss.http;

import com.finalboss.domain.MarketUpdate;
import com.finalboss.useCases.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProducerEventController {
    private static final Logger log = LoggerFactory.getLogger(ProducerEventController.class);
    private final Publisher publisher;


    public ProducerEventController(Publisher publisher) {
        this.publisher = publisher;
   }

    @PostMapping("/marketUpdate")
    public void updateMarket(@RequestBody MarketUpdate marketUpdate) {
        log.info("operation=updateMarket, message='getting a new request', marketUpdate='{}'", marketUpdate);
        publisher.publish(marketUpdate);
        log.info("operation=updateMarket, message='everything worked', marketUpdate='{}'", marketUpdate);
    }



}
