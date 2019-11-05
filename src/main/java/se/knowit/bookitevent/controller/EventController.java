package se.knowit.bookitevent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;
import se.knowit.bookitevent.service.SaveOrUpdateCommand;
import se.knowit.bookitevent.service.SaveOrUpdateCommand.CommandResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static se.knowit.bookitevent.service.SaveOrUpdateCommand.Outcome.CREATED;
import static se.knowit.bookitevent.service.SaveOrUpdateCommand.Outcome.UPDATED;

@RestController
@RequestMapping(EventController.BASE_PATH)
@CrossOrigin()
public class EventController {
	@SuppressWarnings("WeakerAccess")
	static final String BASE_PATH = "/api/v1/events";
	private static final URI BASE_URI = URI.create(BASE_PATH + "/");
    
    private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}
	
	@GetMapping({"", "/"})
	public Set<EventDTO> findAllEvents() {
		Set<Event> allEvents = eventService.findAll();
		if (allEvents.isEmpty()) {
			throw notFound();
		}
		EventMapper mapper = new EventMapper();
		return allEvents.stream().map(mapper::toDTO).collect(toSet());
	}
	
	@GetMapping("/{id}")
	public EventDTO findById(@PathVariable String id) {
		Event event = eventService.findByEventId(UUID.fromString(id)).orElseThrow(this::notFound);
		return new EventMapper().toDTO(event);
	}
	
	@PostMapping(value = {"", "/"}, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> createOrUpdateEvent(@RequestBody EventDTO dto) {
		var result = saveOrUpdate(dto);
		
		if (result.getOutcome() == CREATED) {
			return generateEventCreatedResponse(result);
		} else if (result.getOutcome() == UPDATED) {
			return generateEventUpdateAcceptedResponse(result);
		} else {
			return generateResponseForBadSaveOrUpdateEventRequest(result);
		}
	}
	
	private ResponseEntity<String> generateResponseForBadSaveOrUpdateEventRequest(CommandResult result) {
		Throwable throwable = result.getThrowable().orElseThrow(RuntimeException::new);
		StringWriter writer = new StringWriter();
		throwable.printStackTrace(new PrintWriter(writer));
		return ResponseEntity.badRequest().body(writer.toString());
	}
	
	private ResponseEntity<String> generateEventUpdateAcceptedResponse(CommandResult result) {
		UUID eventId = result.getEventId().orElseThrow(RuntimeException::new);
		return ResponseEntity.accepted().location(getURI(eventId.toString())).build();
	}
	
	private ResponseEntity<String> generateEventCreatedResponse(CommandResult result) {
		UUID eventId = result.getEventId().orElseThrow(RuntimeException::new);
		return ResponseEntity.created(getURI(eventId.toString())).build();
	}
	
	private CommandResult saveOrUpdate(@RequestBody EventDTO dto) {
		var command = new SaveOrUpdateCommand(eventService);
		return command.apply(dto);
	}
	
	private URI getURI(String uri) {
		return URI.create(BASE_URI.getPath() + uri);
	}
	
	private ResponseStatusException notFound() {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
	}
}
