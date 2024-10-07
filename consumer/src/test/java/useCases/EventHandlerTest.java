package useCases;

import com.finalboss.domain.Event;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.Selection;
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

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventHandlerTest {

    @Mock
    YellowEventRepository repoMock;

    @Mock
    YellowEventMapper mapperMock;

    @Mock
    EventPublisher publisherMock;

    @InjectMocks
    EventHandler victim;

    MarketUpdate marketUpdate;

    @BeforeEach
    void setUp() {
        initMocks(this);
        victim = new EventHandler(repoMock, mapperMock, publisherMock);
        marketUpdate= buildMarketUpdate(Operation.ADD);
    }

    @Test
    void readOperationTest() {
        victim.readOperation(marketUpdate);
        verify(victim).addYellowEventToRepository(marketUpdate);
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

}
