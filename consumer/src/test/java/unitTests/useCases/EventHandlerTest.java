package unitTests.useCases;

import com.finalboss.domain.*;
import com.finalboss.mapper.YellowEventMapper;
import com.finalboss.repository.Repository;
import com.finalboss.useCases.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

//TODO study dataproviders
public class EventHandlerTest {

    @Mock
    Repository repoMock;
    @Mock
    YellowEventMapper mapperMock;
    @InjectMocks
    EventHandler victim;

    public static Object[][] dataProvider() {
        return new Object[][]{
                {}
        };
    }

    @BeforeEach
    void setUp() {
        initMocks(this);
        victim = new EventHandler(repoMock, mapperMock);
    }

    @Test
    void addYellowEventToRepository() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.addYellowEventToRepository(yellowEvent)).thenReturn(yellowEvent);

        victim.readOperation(marketUpdate);


        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).addYellowEventToRepository(yellowEvent);
        verifyNoMoreInteractions(repoMock);


    }

    @Test
    void modifyYellowEventToRepository() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.MODIFY);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.modifyYellowEventInRepository(yellowEvent)).thenReturn(yellowEvent);

        victim.readOperation(marketUpdate);


        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).modifyYellowEventInRepository(yellowEvent);
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    void removeYellowEventFromRepository() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.DELETE);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.removeYellowEventFromRepository(yellowEvent)).thenReturn(yellowEvent);

        victim.readOperation(marketUpdate);


        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).removeYellowEventFromRepository(yellowEvent);
        verifyNoMoreInteractions(repoMock);

    }

    private MarketUpdate choseMarketMethod(int option, Operation operation) {
        return switch (option) {
            case 0 -> buildMarketUpdate(operation);
            case 1 -> buildMarketUpdateWithNewMarket(operation);
            default -> null;
        };
    }

    private YellowEvent choseYellowEventMethod(int option, MarketUpdate marketUpdate) {
        return switch (option) {
            case 0 -> buildYellowEvent(marketUpdate);
            case 1 -> buildYellowEventWithNullMarket(marketUpdate);
            case 2 -> buildYellowEventWithEmptyMarket(marketUpdate);
            case 3 -> buildEventWithModifiedMarket(marketUpdate);
            case 4 -> buildEventWithDifferentMarket(marketUpdate);
            default -> null;
        };
    }

    private MarketUpdate buildMarketUpdate(Operation operation) {
        List<Selection> selectionList = new ArrayList<>();
        Selection selectionMock = new Selection(
                1,
                "selection test",
                0.5F
        );
        Selection selectionMock2 = new Selection(
                2,
                "selection test 3",
                0.5F
        );
        selectionList.add(selectionMock);
        selectionList.add(selectionMock2);

        Event event = new Event(
                "1",
                "event mock",
                "23-03-1247"
        );

        return new MarketUpdate(
                "1",
                "update mock",
                operation,
                event,
                selectionList
        );
    }

    private YellowEvent buildYellowEvent(MarketUpdate marketUpdate) {

        Market market = new Market(
                marketUpdate.id(),
                marketUpdate.name(),
                marketUpdate.selections()
        );
        return new YellowEvent(
                marketUpdate.event().id(),
                marketUpdate.event().name(),
                marketUpdate.event().date(),
                List.of(market)
        );
    }

    private YellowEvent buildYellowEventWithNullMarket(MarketUpdate marketUpdate) {
        List<Market> markets = null;
        return new YellowEvent(
                marketUpdate.event().id(),
                marketUpdate.event().name(),
                marketUpdate.event().date(),
                markets
        );
    }

    private YellowEvent buildYellowEventWithEmptyMarket(MarketUpdate marketUpdate) {
        List<Market> markets = Collections.emptyList();
        return new YellowEvent(
                marketUpdate.event().id(),
                marketUpdate.event().name(),
                marketUpdate.event().date(),
                markets
        );
    }

    private MarketUpdate buildMarketUpdateWithNewMarket(Operation operation) {
        List<Selection> selectionList = new ArrayList<>();
        Selection selectionMock = new Selection(
                2,
                "selection test 2",
                0.6F
        );
        Selection selectionMock2 = new Selection(
                3,
                "selection test 3",
                0.4F
        );
        selectionList.add(selectionMock);
        selectionList.add(selectionMock2);

        Event event = new Event(
                "1",
                "event mock",
                "23-03-1247"
        );

        return new MarketUpdate(
                "2",
                "alternative update mock",
                operation,
                event,
                selectionList
        );
    }

    private Market buildMarket(MarketUpdate marketUpdate) {
        return new Market(
                marketUpdate.id(),
                marketUpdate.name(),
                marketUpdate.selections()
        );
    }

    private YellowEvent buildEventWithModifiedMarket(MarketUpdate marketUpdate) {
        List<Selection> selectionList = new ArrayList<>();
        Selection selectionMock = new Selection(
                3,
                "Sel3 this was modified",
                0.6F
        );
        Selection selectionMock2 = new Selection(
                4,
                "Sel4 this was modified",
                0.4F
        );
        selectionList.add(selectionMock);
        selectionList.add(selectionMock2);


        return new YellowEvent(
                marketUpdate.event().id(),
                marketUpdate.event().name(),
                marketUpdate.event().date(),
                List.of(new Market(
                                marketUpdate.id(),
                                "This market was modified",
                                selectionList
                        )
                )
        );
    }

    private YellowEvent buildEventWithDifferentMarket(MarketUpdate marketUpdate) {
        List<Selection> selectionList = new ArrayList<>();
        Selection selectionMock = new Selection(
                3,
                "Sel3 this was modified",
                0.6F
        );
        Selection selectionMock2 = new Selection(
                4,
                "Sel4 this was modified",
                0.4F
        );
        selectionList.add(selectionMock);
        selectionList.add(selectionMock2);


        return new YellowEvent(
                marketUpdate.event().id(),
                marketUpdate.event().name(),
                marketUpdate.event().date(),
                List.of(new Market(
                                "2",
                                "This market was modified",
                                selectionList
                        )
                )
        );
    }


}
