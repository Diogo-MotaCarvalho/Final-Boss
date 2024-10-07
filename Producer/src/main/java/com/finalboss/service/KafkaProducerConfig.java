package com.finalboss.service;

import com.finalboss.domain.MarketUpdate;
import lombok.Data;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public JsonSerializer<MarketUpdate> jsonSerializer() {
        return new JsonSerializer<MarketUpdate>();
    }

    //@Value("${kafka.producers.topics}")
    //private String topic;
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;


    @Bean
    public ProducerFactory<String, MarketUpdate> producerFactory(JsonSerializer<MarketUpdate> jsonSerializer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        configProps.put(BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), jsonSerializer);
    }

    @Bean
    public KafkaTemplate<String, MarketUpdate> kafkaTemplate(ProducerFactory<String, MarketUpdate> producerFactory) {
        return new KafkaTemplate<String,MarketUpdate>(producerFactory);
    }
}
