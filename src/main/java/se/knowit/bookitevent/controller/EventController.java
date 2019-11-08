package se.knowit.bookitevent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.CreateOrUpdateCommand;
import se.knowit.bookitevent.service.CreateOrUpdateCommand.CommandResult;
import se.knowit.bookitevent.service.EventService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static se.knowit.bookitevent.service.CreateOrUpdateCommand.Outcome.CREATED;
import static se.knowit.bookitevent.service.CreateOrUpdateCommand.Outcome.UPDATED;

@RestController
@RequestMapping(EventController.BASE_PATH)
@CrossOrigin()
public class EventController {
    /*Ignore warnings or hints to make this field
     private or the RequestMapping annotation can't see it*/
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
		// apply sorting by EventStart
		List<Event> events = allEvents.stream().collect(Collectors.toList());
		Comparator<Event> eventComparator = Comparator.comparing(Event::getEventStart);
		Collections.sort(events, eventComparator);

		// printouts for testing purpose
		events.stream().forEach(a -> System.out.println(a.getEventStart()));

		EventMapper mapper = new EventMapper();
		return events.stream().map(mapper::toDTO).collect(toSet());
	}

    @GetMapping("/{id}")
    public EventDTO findById(@PathVariable String id) {
        Event event = eventService.findByEventId(UUID.fromString(id)).orElseThrow(this::notFound);
        return new EventMapper().toDTO(event);
    }
    
    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }
    
    @PostMapping(value = {"", "/"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createOrUpdateEvent(@RequestBody EventDTO dto) {
        var result = createOrUpdate(dto);
        
        if (result.getOutcome() == CREATED) {
            return generateEventCreatedResponse(result);
        } else if (result.getOutcome() == UPDATED) {
            return generateEventUpdateAcceptedResponse(result);
        } else {
            return generateResponseForBadCreateOrUpdateEventRequest(result);
        }
    }
    
    private CommandResult createOrUpdate(@RequestBody EventDTO dto) {
        var command = new CreateOrUpdateCommand(eventService);
        return command.apply(dto);
    }
    
    private ResponseEntity<String> generateEventCreatedResponse(CommandResult result) {
        UUID eventId = result.getEventId().orElseThrow(RuntimeException::new);
        return ResponseEntity.created(getURI(eventId.toString()))
                .header("Access-Control-Expose-Headers", "Location").build();
    }
    
    private ResponseEntity<String> generateEventUpdateAcceptedResponse(CommandResult result) {
        UUID eventId = result.getEventId().orElseThrow(RuntimeException::new);
        return ResponseEntity.accepted().location(getURI(eventId.toString()))
                .header("Access-Control-Expose-Headers", "Location").build();
    }
    
    private ResponseEntity<String> generateResponseForBadCreateOrUpdateEventRequest(CommandResult result) {
        Throwable throwable = result.getThrowable().orElseThrow(RuntimeException::new);
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return ResponseEntity.badRequest().body(writer.toString());
    }
    
    
    private URI getURI(String uri) {
        return URI.create(BASE_URI.getPath() + uri);
    }
    
}
