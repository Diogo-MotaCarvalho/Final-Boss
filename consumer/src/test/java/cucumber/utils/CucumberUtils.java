package cucumber.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalboss.port.Consumer;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.net.http.HttpClient.newHttpClient;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class CucumberUtils {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void sendPostRequest(JsonNode postBody) throws IOException, InterruptedException {
        HttpClient client = newHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(postBody);

        HttpRequest post = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/api/marketUpdate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(post, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode())
                .isEqualTo(200);
        assertThat(response.body())
                .isEqualTo("");

    }

    public static Map<String, String> consumeData(String bootstrapServers, String topic, String setGroup,String clientID) {

        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, setGroup);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, clientID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(topic));

        long startTime = System.currentTimeMillis();
        long durationMillis = 5000;

        Map<String, String> returnRecords = new HashMap<>();

        while (System.currentTimeMillis() - startTime < durationMillis) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                log.info("Key: {}, Value: {}", record.key(), record.value());
                log.info("Partition: {}, Offset:{}", record.partition(), record.offset());
                returnRecords.put(record.key(), record.value());
            }
        }
        consumer.close();
        return returnRecords;
    }

    public static Document getEventFromDB(String id) {
        // Replace the placeholder with your Atlas connection string
        String uri = "mongodb://localhost:27017/local";
        // Construct a ServerApi instance using the ServerApi.builder() method
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("local");
            try {
                // Send a ping to confirm a successful connection
                Bson command = new BsonDocument("ping", new BsonInt64(1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");

                MongoCollection<Document> collection = database.getCollection("yellow-events");
                Bson filter = Filters.and(Filters.eq("_id", id));
                return collection.find(filter).first();
            } catch (MongoException me) {
                System.err.println(me);
            }
        }

        return null;
    }

    public static void clearCollection() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/local");
        MongoDatabase database = mongoClient.getDatabase("local");
        MongoCollection<Document> collection = database.getCollection("yellow-events");
        collection.drop();
    }

}
