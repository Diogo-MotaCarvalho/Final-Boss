package unitTests;

import com.finalboss.domain.Event;
import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.Operation;
import com.finalboss.domain.Selection;
import com.finalboss.useCases.KafkaPublisher;
import org.junit.jupiter.api.*;



import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KafkaPublisherTest {

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    Event event= new Event("3","Benfica vs Sporting","27/10/2023");

    List<Selection> selections = new ArrayList<>();


    MarketUpdate update= new MarketUpdate("2", "Match Odds", Operation.ADD, event,selections );

    @Mock
    CompletableFuture<SendResult<String, MarketUpdate>> future;

    @Mock
    KafkaTemplate<String,MarketUpdate> kafkaTemplate;

    @InjectMocks
    KafkaPublisher victim;

    @Test
    void testSendSuccess() {
        when(kafkaTemplate.send(topic,update)).thenReturn(future);
        victim.publish(update);
        verify(kafkaTemplate).send(Mockito.anyString(),Mockito.any());
    }




}
