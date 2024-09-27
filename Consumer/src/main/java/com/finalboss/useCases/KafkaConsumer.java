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

        JsonNode selectionsArray =recordData.get("selections");

        List<Market> markets = new ArrayList<>();
        Market market = new Market(
                recordData.get("id").asText(  "null"),
                recordData.get("name").asText(  "null"),
                objectMapper.convertValue(selectionsArray, List.class)
        );
        markets.add(market);

        JsonNode recordEvent = recordData.get("event");
        YellowEvent yellowEvent = new YellowEvent(
                recordEvent.get("id").asText(  "null"),
                recordEvent.get("name").asText(  "null"),
                recordEvent.get("date").asText(  "null"),
                markets
        );

        //Unused for now
        String recordOperation= recordData.get("operation").asText(  "null");


        System.out.println(yellowEvent);
        return yellowEvent;
    }


}
