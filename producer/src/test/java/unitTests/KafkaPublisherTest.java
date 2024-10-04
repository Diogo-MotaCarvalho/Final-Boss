package unitTests;

import com.finalboss.domain.MarketUpdate;
import com.finalboss.port.KafkaPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

class KafkaPublisherTest {
    @Mock
    MarketUpdate update;

    @Mock
    KafkaTemplate<String, MarketUpdate> kafkaTemplate;

    String topic = "demo";

    KafkaPublisher victim;

    @BeforeEach
    void setup() {
        initMocks(this);
        victim = new KafkaPublisher(topic, kafkaTemplate);
    }

    @Test
    void testSendSuccess() {
        victim.publish(update);
        verify(kafkaTemplate).send(topic, update);
    }

}
