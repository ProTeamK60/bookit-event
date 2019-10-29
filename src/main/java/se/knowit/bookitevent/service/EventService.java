package se.knowit.bookitevent.service;

import java.util.Optional;
import java.util.UUID;

import se.knowit.bookitevent.model.Event;

public interface EventService extends CrudService<Event, Long> {

	Optional<Event> findByEventId(UUID id);
}
