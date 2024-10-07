package port;

import com.finalboss.domain.MarketUpdate;
import com.finalboss.port.Consumer;
import com.finalboss.useCases.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConsumerTest {

    @Mock
    EventHandler eventHandler;

    @Mock
    MarketUpdate marketUpdate;

    Consumer victim;

    @BeforeEach
    void setUp() {
        initMocks(this);
        victim = new Consumer(eventHandler);
    }

    @Test
    public void consumerTest() {
        victim.onMessage(marketUpdate);
        verify(eventHandler).readOperation(marketUpdate);
    }
}
