package se.knowit.bookitevent.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import se.knowit.bookitevent.model.Event;

public interface EventRepository extends CrudRepository<Event, Long>{
	
	Optional<Event> findByEventId(UUID id);
}
