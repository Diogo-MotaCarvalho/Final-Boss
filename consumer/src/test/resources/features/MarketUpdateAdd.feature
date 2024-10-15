Feature: MarketUpdate Add
  Scenario: Creating a new YellowEvent
    When POST request at localhost:8083/api/marketUpdate is received
    Then MarketUpdate is published at "demo" topic
    And YellowEvent is published at "events" topic
    And YellowEvent is created in db
