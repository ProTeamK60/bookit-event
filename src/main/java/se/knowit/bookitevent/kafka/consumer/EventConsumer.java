package se.knowit.bookitevent.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.service.EventService;

import java.lang.invoke.MethodHandles;

public class EventConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    
    public EventConsumer(EventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(id = "event-listener",
            topics = {"events"},
            groupId = "event-consumer",
            containerFactory = "kafkaListenerContainerFactory",
            topicPartitions = @TopicPartition(
                    topic = "events",
                    partitionOffsets = @PartitionOffset(
                            partition = "0",
                            initialOffset = "0"
                    )
            )
    )
    public void consumeMessage(EventDTO event) {
        LOG.trace("Consume event {}", event);
        eventService.createOrUpdate(event);
    }

}
