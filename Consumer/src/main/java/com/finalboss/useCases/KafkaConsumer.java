package com.finalboss.useCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.finalboss.domain.Market;
import com.finalboss.domain.Selection;
import com.finalboss.domain.YellowEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.yaml.snakeyaml.error.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class KafkaConsumer implements Consumer {

    private static final Logger log = Logger.getLogger(KafkaConsumer.class.getName());

    ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String,?> kafkaTemplate;

    public KafkaConsumer(KafkaTemplate<String, ?> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public YellowEvent buildYellowEvent(@RequestBody ConsumerRecord<String, String> record) throws JsonProcessingException {

        JsonNode recordData = objectMapper.readTree(record.value());

        System.out.println(recordData.toString());

        String recordId = recordData.get("id").toString();

        String recordName = recordData.get("name").asText(  "null");
        String recordOperation= recordData.get("operation").asText(  "null");

        JsonNode recordEvent = recordData.get("event");
        String eventId = recordEvent.get("id").asText(  "null");
        String eventName = recordEvent.get("name").asText(  "null");
        String eventDate = recordEvent.get("date").asText(  "null");

        JsonNode selectionsArray =recordData.get("selections");
        List<Selection> selections = objectMapper.convertValue(selectionsArray, List.class);

        List<Market> markets = new ArrayList<>();

        Market market = new Market(recordId,recordName,selections);
        markets.add(market);

        YellowEvent yellowEvent = new YellowEvent(eventId,eventName,eventDate ,markets);

        System.out.println(yellowEvent);


        return yellowEvent;
    }


}
