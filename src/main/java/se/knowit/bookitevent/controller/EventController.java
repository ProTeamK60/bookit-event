package se.knowit.bookitevent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static se.knowit.bookitevent.service.EventService.Outcome.*;

@RestController
@RequestMapping(EventController.BASE_PATH)
@CrossOrigin()
public class EventController {
    /*Ignore warnings or hints to make this field
     private or the RequestMapping annotation can't see it*/
    static final String BASE_PATH = "/api/v1/events";
    private static final URI BASE_URI = URI.create(BASE_PATH + "/");
    
    private final EventService eventService;
    private final EventMapper mapper;
    
    public EventController(final EventService eventService) {
        this.eventService = eventService;
        mapper = new EventMapper();
    }
    
    @GetMapping({"", "/"})
    public Set<EventDTO> findAllEvents() {
        Set<Event> allEvents = eventService.findAll();
        if (allEvents.isEmpty()) {
            throw notFound();
        }
        
        return allEvents.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toSet());
	}

    @GetMapping("/{id}")
    public EventDTO findById(@PathVariable String id) {
        Event event = eventService.findByEventId(UUID.fromString(id)).orElseThrow(this::notFound);
        return mapper.toDTO(event);
    }

    @DeleteMapping("/{id}")
    public EventDTO markDeletedByEventId(@PathVariable String id, HttpServletResponse response) {
        Event event = eventService.markDeletedByEventId(UUID.fromString(id)).orElseThrow(this::notFound);
        // inform the client that the request is accepted and the resource is queued for deletion.
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        return mapper.toDTO(event);
    }

    @GetMapping("/sorted/eventstart")
    public List<EventDTO> findAllEventsSortedByEventStart() {
        Set<EventDTO> eventDTOS = findAllEvents();
        // apply sorting by EventStart
        Comparator<EventDTO> eventComparator = Comparator.comparing(EventDTO::getEventStart);
        return eventDTOS.stream()
                .sorted(eventComparator)
                .collect(Collectors.toList());
    }
    
    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
    }
    
    @PostMapping(value = {"", "/"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createOrUpdateEvent(@RequestBody EventDTO dto) {
        var result = eventService.createOrUpdate(dto);

        if (result.getOutcome() == CREATED) {
            return generateEventCreatedResponse(result);
        } else if (result.getOutcome() == UPDATED || result.getOutcome() == NO_CHANGE) {
            return generateEventUpdateAcceptedResponse(result);
        } else {
            return generateResponseForBadCreateOrUpdateEventRequest(result);
        }
    }
    
    private ResponseEntity<String> generateEventCreatedResponse(EventService.CreateOrUpdateCommandResult result) {
        UUID eventId = result.getEventId().orElseThrow(RuntimeException::new);
        return ResponseEntity.created(getURI(eventId.toString()))
                .header("Access-Control-Expose-Headers", "Location").build();
    }
    
    private ResponseEntity<String> generateEventUpdateAcceptedResponse(EventService.CreateOrUpdateCommandResult result) {
        UUID eventId = result.getEventId().orElseThrow(RuntimeException::new);
        return ResponseEntity.accepted().location(getURI(eventId.toString()))
                .header("Access-Control-Expose-Headers", "Location").build();
    }
    
    private ResponseEntity<String> generateResponseForBadCreateOrUpdateEventRequest(EventService.CreateOrUpdateCommandResult result) {
        Throwable throwable = result.getThrowable().orElseThrow(RuntimeException::new);
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return ResponseEntity.badRequest().body(writer.toString());
    }
    
    
    private URI getURI(String uri) {
        return URI.create(BASE_URI.getPath() + uri);
    }
    
}
