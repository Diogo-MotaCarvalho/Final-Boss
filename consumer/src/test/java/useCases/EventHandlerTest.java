package useCases;

import com.finalboss.domain.*;
import com.finalboss.mapper.YellowEventMapper;
import com.finalboss.port.EventPublisher;
import com.finalboss.repository.YellowEventRepository;
import com.finalboss.useCases.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

//TODO study dataproviders
public class EventHandlerTest {

    @Mock
    YellowEventRepository repoMock;
    @Mock
    YellowEventMapper mapperMock;
    @Mock
    EventPublisher publisherMock;
    @InjectMocks
    EventHandler victim;


    @BeforeEach
    void setUp() {
        initMocks(this);
        victim = new EventHandler(repoMock, mapperMock, publisherMock);
    }

    @Test
    void existingEventAndMarketAddTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.ofNullable(yellowEvent));

        victim.readOperation(marketUpdate);

        verify(repoMock).findById(yellowEvent.id());
        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(repoMock);
        verifyNoMoreInteractions(mapperMock);
        verifyNoInteractions(publisherMock);
    }

    @Test
    void existingEventButNullMarketAddTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEventWithNullMarket(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.ofNullable(yellowEvent));

        assertThrows(NullPointerException.class, () -> victim.readOperation(marketUpdate));
    }

    @Test
    void existingEventButNoMarketAddTest() {

        MarketUpdate marketUpdateOnRepo = buildMarketUpdateWithNewMarket(Operation.ADD);
        YellowEvent yellowEventOnRepo = buildYellowEvent(marketUpdateOnRepo);
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        ArrayList<Market> expectedMarkets = new ArrayList<>();
        expectedMarkets.addAll(yellowEvent.markets());
        expectedMarkets.addAll(yellowEventOnRepo.markets());
        YellowEvent expectedYellowEvent = new YellowEvent(
                yellowEvent.id(),
                yellowEvent.name(),
                yellowEvent.date(),
                expectedMarkets
        );


        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEventOnRepo.id())).thenReturn(Optional.of(yellowEventOnRepo));
        when(mapperMock.buildMarket(marketUpdate)).thenReturn(buildMarket(marketUpdate));

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEventOnRepo.id());
        verify(repoMock).save(expectedYellowEvent);
        verifyNoMoreInteractions(repoMock);
        verify(publisherMock).publish(expectedYellowEvent);
        verifyNoMoreInteractions(publisherMock);
    }

    @Test
    void notExistingEventWithNewMarketAddTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.empty());
        when(repoMock.save(yellowEvent)).thenReturn(yellowEvent);
        when(publisherMock.publish(yellowEvent)).thenReturn(yellowEvent);

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verify(repoMock).save(yellowEvent);
        verifyNoMoreInteractions(repoMock);
        verify(publisherMock).publish(yellowEvent);
        verifyNoMoreInteractions(publisherMock);
    }

    @Test
    void notExistingEventModifyTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.MODIFY);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.empty());

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verifyNoMoreInteractions(repoMock);
        verifyNoInteractions(publisherMock);
    }

    @Test
    void existingEventButNoMarketModifyTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.MODIFY);
        YellowEvent yellowEvent = buildYellowEventWithNullMarket(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.of(yellowEvent));

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verifyNoMoreInteractions(repoMock);
        verifyNoInteractions(publisherMock);
    }

    @Test
    void existingEventBuMarketNotInRepoModifyTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.MODIFY);
        YellowEvent yellowEvent = buildYellowEventWithNullMarket(marketUpdate);
        YellowEvent yellowEventOnRepo = buildEventWithDifferentMarket(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.of(yellowEventOnRepo));
        when(repoMock.save(yellowEventOnRepo)).thenReturn(yellowEventOnRepo);

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verifyNoMoreInteractions(repoMock);
        verifyNoInteractions(publisherMock);
    }

    @Test
    void existingEventAndMarketModifyTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.MODIFY);
        YellowEvent yellowEvent = buildEventWithModifiedMarket(marketUpdate);

        MarketUpdate marketUpdateOnRepo = buildMarketUpdate(Operation.MODIFY);
        YellowEvent yellowEventOnRepo = buildYellowEvent(marketUpdateOnRepo);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.of(yellowEventOnRepo));
        when(repoMock.save(yellowEvent)).thenReturn(yellowEvent);
        when(publisherMock.publish(yellowEvent)).thenReturn(yellowEvent);

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verify(repoMock).save(yellowEvent);
        verifyNoMoreInteractions(repoMock);
        verify(publisherMock).publish(yellowEvent);
        verifyNoMoreInteractions(publisherMock);
    }

    @Test
    void existingEventAndMarketRemoveTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.DELETE);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);
        YellowEvent yellowEventWithNoMarket = buildYellowEventWithEmptyMarket(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.of(yellowEvent));
        when(repoMock.save(yellowEventWithNoMarket)).thenReturn(yellowEventWithNoMarket);
        when(publisherMock.publish(yellowEventWithNoMarket)).thenReturn(yellowEventWithNoMarket);

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verify(repoMock).save(yellowEventWithNoMarket);
        verifyNoMoreInteractions(repoMock);
        verify(publisherMock).publish(yellowEventWithNoMarket);
        verifyNoMoreInteractions(publisherMock);


    }

    @Test
    void existingEventButNotMarketRemoveTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.DELETE);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);
        YellowEvent yellowEventOnRepo = buildEventWithDifferentMarket(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.of(yellowEventOnRepo));

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verifyNoMoreInteractions(repoMock);
        verifyNoInteractions(publisherMock);
    }

    @Test
    void notExistentEventRemoveTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.DELETE);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);


        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.empty());

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verifyNoMoreInteractions(repoMock);
        verifyNoInteractions(publisherMock);
    }

    @Test
    void saveWithTryCatchThrowsTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.empty());
        when(repoMock.save(yellowEvent)).thenThrow(IllegalArgumentException.class);

        assertThrows(RuntimeException.class, () -> victim.readOperation(marketUpdate));
    }


//    public static Object[][] dataProvider(){
//        return new Object[][]{
//                {}
//        }
//    }

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
