package cucumber.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.YellowEvent;
import com.finalboss.mapper.YellowEventMapper;
import cucumber.utils.CucumberUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MarketUpdateAddSteps {

    @When("^POST request at localhost:(\\d+)/api/marketUpdate is received$")
    public void postRequestAtLocalhostApiMarketUpdateIsRecieved(int arg0) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("src/test/resources/marketUpdate.json"));
        CucumberUtils.sendPostRequest(jsonNode);
        System.out.println("POST request at localhost:" + arg0 + " went ok");
    }

    @Then("^MarketUpdate is published at \"([^\"]*)\" topic$")
    public void marketupdateIsPublishedAtTopic(String arg0) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("src/test/resources/marketUpdate.json"));
        String bootstrapServers = "localhost:9092";
        String topic = "demo";
        Map<String, String> records = CucumberUtils.consumeData(bootstrapServers, topic, "final-boss-consumer");

        assertThat(records.containsValue(jsonNode.toString())).isTrue();
    }

    @And("^YellowEvent is published at \"([^\"]*)\" topic$")
    public void yelloweventIsPublishedAtTopic(String arg0) throws Throwable {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("src/test/resources/marketUpdate.json"));
        MarketUpdate marketUpdate = objectMapper.treeToValue(jsonNode, MarketUpdate.class);
        YellowEventMapper yellowEventMapper = new YellowEventMapper();
        YellowEvent event = yellowEventMapper.buildYellowEvent(marketUpdate);

        String bootstrapServers = "localhost:9092";
        String topic = "events";

        Map<String, String> records = CucumberUtils.consumeData(bootstrapServers, topic, "final-boss-producer");

        assertThat(records.containsValue(objectMapper.writeValueAsString(event))).isTrue();

    }

    @And("^YellowEvent is created in db$")
    public void yelloweventIsCreatedInDb() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedJsonNode = objectMapper.readTree(new File("src/test/resources/yellowEvent.json"));

        String actual = CucumberUtils.getEventFromDB("2578936").toJson().replace(": ",":").replace(", ",",");

       assertThat(actual).isEqualTo(objectMapper.writeValueAsString(expectedJsonNode));
    }
}
