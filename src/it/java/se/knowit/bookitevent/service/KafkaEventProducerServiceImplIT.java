package se.knowit.bookitevent.service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1,
        topics = {KafkaEventProducerServiceImplIT.TOPIC_NAME},
        brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@SpringBootTest
public class KafkaEventProducerServiceImplIT {

    public static final String TOPIC_NAME = "events";

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private EmbeddedKafkaBroker kafkaBroker;

    @Test
    public void test_SendEvent_ToTopic() {
        EventDTO savedEvent = new EventDTO();
        savedEvent.setEventId(UUID.randomUUID().toString());
        savedEvent.setName("Test Event");
        savedEvent.setDescription("desc");
        savedEvent.setLocation("loc");
        savedEvent.setOrganizer("org");
        savedEvent.setEventStart(20000L);
        savedEvent.setEventEnd(30000L);
        savedEvent.setDeadlineRVSP(10000L);
        kafkaProducerService.sendMessage(savedEvent);

        final Consumer<String, EventDTO> consumer = buildConsumer();
        kafkaBroker.consumeFromAnEmbeddedTopic(consumer, TOPIC_NAME);
        final ConsumerRecord<String, EventDTO> record = getSingleRecord(consumer);
        assertEquals(savedEvent, record.value());
    }


    private Consumer<String, EventDTO> buildConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testSendEvent", "true", kafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        ConsumerFactory<String, EventDTO> consumerFactory = new DefaultKafkaConsumerFactory(consumerProps, new StringDeserializer(), new JsonDeserializer(EventDTO.class, false));
        return consumerFactory.createConsumer();
    }

    private ConsumerRecord<String, EventDTO> getSingleRecord(Consumer<String, EventDTO> consumer) {
        final ConsumerRecords<String, EventDTO> records = consumer.poll(Duration.ofSeconds(1L));
        return records.iterator().next();
    }

}
