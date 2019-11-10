package se.knowit.bookitevent.dto;

import se.knowit.bookitevent.model.Event;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class EventMapper {
    
    public Event fromDTO(EventDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setOrganizer(dto.getOrganizer());
        if (notNullOrBlank(dto.getEventId())) {
            event.setEventId(UUID.fromString(dto.getEventId()));
        }
        event.setEventStart(getInstant(dto.getEventStart()));
        event.setEventEnd(getInstant(dto.getEventEnd()));
        event.setDeadlineRVSP(getInstant(dto.getDeadlineRVSP()));
        return event;
    }
    
    public EventDTO toDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setOrganizer(event.getOrganizer());
        if (event.getEventId() != null) {
            dto.setEventId(event.getEventId().toString());
        }
        Instant eventStart = event.getEventStart();
        Long epochMilli = getEpochMilli(eventStart);
        Instant instant = getInstant(epochMilli);
        dto.setEventStart(epochMilli);
        dto.setEventEnd(getEpochMilli(event.getEventEnd()));
        dto.setDeadlineRVSP(getEpochMilli(event.getDeadlineRVSP()));
        return dto;
    }
    
    private boolean notNullOrBlank(String test) {
        return test != null && !test.isBlank();
    }
    private Instant getInstant(Long input) {
        return Optional.ofNullable(input).map(Instant::ofEpochMilli).orElse(null);
    }
    private Long getEpochMilli(Instant input) {
        return Optional.ofNullable(input).map(Instant::toEpochMilli).orElse(null);
    }
}
