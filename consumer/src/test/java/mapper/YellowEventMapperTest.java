package mapper;

import com.finalboss.domain.*;
import com.finalboss.mapper.YellowEventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class YellowEventMapperTest {

    MarketUpdate marketUpdate;
    Event event;
    YellowEvent yellowEvent;
    YellowEventMapper victim;


    @BeforeEach
    void setUp() {
        victim = new YellowEventMapper();
        marketUpdate = buildMarketUpdate();
    }

    @Test
    void buildYellowEventTest() {

        yellowEvent = victim.buildYellowEvent(marketUpdate);
        List<Market> markets = new ArrayList<>();
        Market market = victim.buildMarket(marketUpdate);
        markets.add(market);

        assertNotNull(yellowEvent);
        assertEquals(yellowEvent.id(), event.id());
        assertEquals(yellowEvent.name(), event.name());
        assertEquals(yellowEvent.date(), event.date());
        assertEquals(yellowEvent.markets().toString(), markets.toString());

    }

    @Test
    void buildMarketTest() {

        Market market = victim.buildMarket(marketUpdate);

        assertNotNull(market);
        assertEquals(marketUpdate.id(), market.id());
        assertEquals(marketUpdate.name(), market.name());
        assertEquals(marketUpdate.selections().toString(), marketUpdate.selections().toString());

    }

    @Test
    void buildMarketNullArgTest() {
        assertThrows(IllegalArgumentException.class, () -> victim.buildMarket(null));
    }

    @Test
    void buildYellowEventsArgParamTest() {
        assertThrows(IllegalArgumentException.class, () -> victim.buildYellowEvent(null));
    }

    private MarketUpdate buildMarketUpdate() {
        List<Selection> selectionList = new ArrayList<>();
        Selection selectionMock = new Selection(
                1,
                "selection test",
                0.4F
        );
        selectionList.add(selectionMock);

        event = new Event(
                "1",
                "event mock",
                "23-03-1247"
        );

        return marketUpdate = new MarketUpdate(
                "1",
                "update mock",
                Operation.ADD,
                event,
                selectionList
        );
    }

}
