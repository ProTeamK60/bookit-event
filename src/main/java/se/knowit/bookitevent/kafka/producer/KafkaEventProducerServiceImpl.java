package se.knowit.bookitevent.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import se.knowit.bookitevent.dto.EventDTO;

public class KafkaEventProducerServiceImpl implements KafkaProducerService<String, EventDTO> {

    private KafkaTemplate<String, EventDTO> eventTemplate;

    public KafkaEventProducerServiceImpl(KafkaTemplate<String, EventDTO> eventTemplate) {
        this.eventTemplate = eventTemplate;
    }

    @Override
    public void sendMessage(final String topic, final String key, final EventDTO event) {
        ListenableFuture<SendResult<String, EventDTO>> future = eventTemplate.send(topic, key, event);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                //TODO: add logging.
            }

            @Override
            public void onSuccess(SendResult<String, EventDTO> result) {
                //TODO: add logging.
            }
        });
    }

}
