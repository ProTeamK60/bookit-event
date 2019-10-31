package se.knowit.bookitevent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin()
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping("/events")
	public Set<Event> findAllEvents() {
		Set<Event> all = eventService.findAll();
		if (all.isEmpty()) {
			throw notFound();
		}
		return all;
	}
	
	@GetMapping("/events/{id}")
	public Event findById(@PathVariable String id) {
		return eventService.findByEventId(UUID.fromString(id)).orElseThrow(this::notFound);
	}

	private ResponseStatusException notFound() {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}
}
