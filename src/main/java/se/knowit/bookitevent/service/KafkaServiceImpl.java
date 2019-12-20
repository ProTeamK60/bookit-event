package se.knowit.bookitevent.service;

import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.model.Event;

public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    public KafkaServiceImpl(final KafkaTemplate<String, EventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(final String topic, final EventDTO message) {
        final ListenableFuture<SendResult<String, EventDTO>> listenableFuture = kafkaTemplate.send(topic, message);

        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                //TODO: Add logging
            }

            @Override
            public void onSuccess(final SendResult<String, EventDTO> sendResult) {
                //TODO: Add logging
            }
        });
    }
}
