package unitTests;

import com.finalboss.domain.Event;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.Selection;
import com.finalboss.http.ProducerEventController;
import com.finalboss.useCases.KafkaPublisher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ProducerEventControllerTest {

    Event event= new Event("3","Benfica vs Sporting","27/10/2023");

    List<Selection> selections = new ArrayList<>();
    MarketUpdate update= new MarketUpdate("2", "Match Odds", Operation.ADD, event,selections );

    @Mock
    KafkaPublisher publisher;


    @InjectMocks
    ProducerEventController victim;

    @Test
    void publishTest(){

        victim.updateMarket(update);
        verify(publisher).publish(update);


    }

}
