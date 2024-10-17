package unitTests.repository;

import com.finalboss.domain.*;
import com.finalboss.mapper.YellowEventMapper;
import com.finalboss.port.EventPublisher;
import com.finalboss.repository.Repository;
import com.finalboss.repository.YellowEventRepository;
import com.finalboss.useCases.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RepositoryTest {
    @Mock
    YellowEventRepository repoMock;
    @Mock
    EventPublisher publisherMock;
    @InjectMocks
    Repository victim;

    @BeforeEach
    void setUp() {
        initMocks(this);
        victim = new Repository(repoMock, publisherMock);
    }

    @Test
    void existingEventAndMarketAddTest(){

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
    private Market buildMarket(MarketUpdate marketUpdate) {
        return new Market(
                marketUpdate.id(),
                marketUpdate.name(),
                marketUpdate.selections()
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
}
