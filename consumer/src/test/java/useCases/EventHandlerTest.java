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

    //Testar um yellowEvent que existe, com um mercado que já existe.
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
    }

    //Testar um yellowEvent existente mas com mercado Null
    @Test
    void existingEventButNullMarketAddTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEventWithEmptyMarket(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.ofNullable(yellowEvent));

        assertThrows(NullPointerException.class, () -> victim.readOperation(marketUpdate));
    }

    //Testa um evento existente mas sem mercado existente
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
    void newEventWithNewMarketAddTest() {
        MarketUpdate marketUpdate = buildMarketUpdate(Operation.ADD);
        YellowEvent yellowEvent = buildYellowEvent(marketUpdate);

        when(mapperMock.buildYellowEvent(marketUpdate)).thenReturn(yellowEvent);
        when(repoMock.findById(yellowEvent.id())).thenReturn(Optional.empty());

        victim.readOperation(marketUpdate);

        verify(mapperMock).buildYellowEvent(marketUpdate);
        verifyNoMoreInteractions(mapperMock);
        verify(repoMock).findById(yellowEvent.id());
        verify(repoMock).save(yellowEvent);
        verifyNoMoreInteractions(repoMock);
        verify(publisherMock).publish(yellowEvent);
        verifyNoMoreInteractions(publisherMock);
    }


    private MarketUpdate buildMarketUpdate(Operation operation) {
        List<Selection> selectionList = new ArrayList<>();
        Selection selectionMock = new Selection(
                1,
                "selection test",
                0.4F
        );
        selectionList.add(selectionMock);

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

    private YellowEvent buildYellowEventWithEmptyMarket(MarketUpdate marketUpdate) {
        List<Market> markets = null;
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
                1,
                "selection test",
                0.4F
        );
        selectionList.add(selectionMock);

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
}
