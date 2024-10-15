package cucumber.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.utils.CucumberUtils;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.File;
import java.io.IOException;


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
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Market update is Published");
        throw new PendingException();
    }

    @And("^YellowEvent is published at \"([^\"]*)\" topic$")
    public void yelloweventIsPublishedAtTopic(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Yellow Event isPublished");
        throw new PendingException();
    }

    @And("^YellowEvent is created in db$")
    public void yelloweventIsCreatedInDb() {
        System.out.println("Yellow Event is created in db");
    }
}
