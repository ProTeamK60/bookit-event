package se.knowit.bookitevent.repository;

import org.springframework.data.repository.CrudRepository;

import se.knowit.bookitevent.model.Event;

public interface EventRepository extends CrudRepository<Event, Long>{
	
	

}
