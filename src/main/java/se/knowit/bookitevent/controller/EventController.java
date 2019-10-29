package se.knowit.bookitevent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping("events/{id}")
	public Event getById(@PathVariable(value = "id") String id) {
		return eventService.findByEventId(UUID.fromString(id)).orElseThrow(this::notFound);
	}

	private ResponseStatusException notFound() {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");

	}
}
