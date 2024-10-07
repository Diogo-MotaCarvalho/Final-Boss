package port;

import com.finalboss.domain.YellowEvent;
import com.finalboss.port.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class EventPublisherTest {


    @Mock
    YellowEvent yellowEvent;
    String topic = "test";
    EventPublisher victim;
    @Mock
    private KafkaTemplate<String, YellowEvent> kafkaTemplate;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        victim = new EventPublisher(topic, kafkaTemplate);
    }

    @Test
    void testPublish() {
        victim.publish(yellowEvent);
        verify(kafkaTemplate).send(topic, yellowEvent);
    }
}
