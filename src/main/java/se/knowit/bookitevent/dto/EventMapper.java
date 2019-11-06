package se.knowit.bookitevent.dto;

import se.knowit.bookitevent.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class EventMapper {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    public Event fromDTO(EventDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setOrganizer(dto.getOrganizer());
        if (notNullOrBlank(dto.getEventId())) {
            event.setEventId(UUID.fromString(dto.getEventId()));
        }
        if (notNullOrBlank(dto.getEventStart())) {
            event.setEventStart(LocalDateTime.parse(dto.getEventStart(), dateFormatter));
        }
        if (notNullOrBlank(dto.getEventEnd())) {
            event.setEventEnd(LocalDateTime.parse(dto.getEventEnd(), dateFormatter));
        }
        if (notNullOrBlank(dto.getDeadlineRVSP())) {
            event.setDeadlineRVSP(LocalDateTime.parse(dto.getDeadlineRVSP(), dateFormatter));
        }
        return event;
    }
    
    public EventDTO toDTO(Event event){
        EventDTO dto = new EventDTO();
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setOrganizer(event.getOrganizer());
        if (event.getEventId() != null) {
            dto.setEventId(event.getEventId().toString());
        }
        if (event.getEventStart() != null) {
            dto.setEventStart(event.getEventStart().toString());
        }
        if (event.getEventEnd() != null) {
            dto.setEventEnd(event.getEventEnd().toString());
        }
        if (event.getDeadlineRVSP() != null) {
            dto.setDeadlineRVSP(event.getDeadlineRVSP().toString());
        }
        return dto;
    }
    
    private boolean notNullOrBlank(String test) {
        return test != null && !test.isBlank();
    }
}
