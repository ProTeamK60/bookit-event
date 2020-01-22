package se.knowit.bookitevent.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import se.knowit.bookitevent.dto.EventDTO;

public class KafkaEventProducerServiceImpl implements KafkaProducerService<EventDTO> {

    private static final String TOPIC = "events";

    private KafkaTemplate<String, EventDTO> eventTemplate;

    public KafkaEventProducerServiceImpl(KafkaTemplate<String, EventDTO> eventTemplate) {
        this.eventTemplate = eventTemplate;
    }

    @Override
    public void sendMessage(final EventDTO event) {
        ListenableFuture<SendResult<String, EventDTO>> future = eventTemplate.send(TOPIC, event);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                //TODO: When logging is implemented, this should be logged instead with Warn/severe lvl.
                System.out.println("Unable to send message=["
                        + event + "] due to : " + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, EventDTO> result) {
                //TODO: When logs are implemented, this should be logged instead with DEBUG level.
                System.out.println("Sent message=[" + event +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }

}
