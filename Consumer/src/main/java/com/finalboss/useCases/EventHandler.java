package com.finalboss.useCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalboss.domain.Market;
import com.finalboss.domain.YellowEvent;
import com.finalboss.service.YellowEventRepositoryI;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class EventHandler implements EventHandlerI {

    private static final Logger log = Logger.getLogger(EventHandler.class.getName());

    ObjectMapper objectMapper = new ObjectMapper();

    private YellowEventRepositoryI yellowEventRepositoryI;

    private final KafkaTemplate<String,?> kafkaTemplate;

    public EventHandler(KafkaTemplate<String, ?> kafkaTemplate, YellowEventRepositoryI yellowEventRepositoryI) {
        this.yellowEventRepositoryI = yellowEventRepositoryI;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void readOperation(ConsumerRecord<String, String> record) throws JsonProcessingException {
        JsonNode recordData = objectMapper.readTree(record.value());
        String recordOperation= recordData.get("operation").asText(  "null");
        if(recordOperation.equals("ADD")){
            addYellowEventToRepository(record);
        }else if(recordOperation.equals("MODIFY")){

        }else if(recordOperation.equals("DELETE")){

        }
    }

    public void addYellowEventToRepository(ConsumerRecord<String, String> record) throws JsonProcessingException {
        JsonNode recordData = objectMapper.readTree(record.value());

        if(yellowEventRepositoryI.existsById(recordData.get("id").asText(  "null"))){
            if(yellowEventRepositoryI.existsById(buildMarket(record).id())){

            }
        }else {
            yellowEventRepositoryI.save(buildYellowEvent(record));
        }


    }

    //Metodos auxiloares


    public YellowEvent buildYellowEvent(ConsumerRecord<String, String> record) throws JsonProcessingException {

        JsonNode recordData = objectMapper.readTree(record.value());

        List<Market> markets = new ArrayList<>();
        buildMarket(record);
        markets.add(buildMarket(record));

        JsonNode recordEvent = recordData.get("event");
        YellowEvent yellowEvent = new YellowEvent(
                recordEvent.get("id").asText(  "null"),
                recordEvent.get("name").asText(  "null"),
                recordEvent.get("date").asText(  "null"),
                markets
        );

        System.out.println(yellowEvent);
        return yellowEvent;
    }

    public Market buildMarket(ConsumerRecord<String, String> record) throws JsonProcessingException {

        JsonNode recordData = objectMapper.readTree(record.value());

        JsonNode selectionsArray =recordData.get("selections");


        Market market = new Market(
                recordData.get("id").asText(  "null"),
                recordData.get("name").asText(  "null"),
                objectMapper.convertValue(selectionsArray, List.class)
        );

        return market;
    }
}
