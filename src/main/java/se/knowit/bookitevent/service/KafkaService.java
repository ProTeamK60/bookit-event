package se.knowit.bookitevent.service;

import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.model.Event;

public interface KafkaService {
    void sendMessage(final String topic, final EventDTO value);
}
