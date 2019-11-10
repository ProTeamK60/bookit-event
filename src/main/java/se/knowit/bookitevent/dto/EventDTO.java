package se.knowit.bookitevent.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Optional;

@Data
public class EventDTO {
    private String eventId;
    private String name;
    private String description;
    private Instant eventStart;
    private Instant eventEnd;
    private Instant deadlineRVSP;
    private String location;
    private String organizer;
    
    public Long getEventStart() {
        return convertToEpochMillis(eventStart);
    }
    
    public void setEventStart(Long eventStart) {
        this.eventStart = convertToInstant(eventStart);
    }
    
    public Long getEventEnd() {
        return convertToEpochMillis(eventEnd);
    }
    
    public void setEventEnd(Long eventEnd) {
        this.eventEnd = convertToInstant(eventEnd);
    }
    
    
    public Long getDeadlineRVSP() {
        return convertToEpochMillis(deadlineRVSP);
    }
    
    private Instant convertToInstant(Long eventEnd) {
        
        return Instant.ofEpochMilli(eventEnd);
    }
    
    public void setDeadlineRVSP(Long deadlineRVSP) {
        this.deadlineRVSP = convertToInstant(deadlineRVSP);
    }
    
    private Long convertToEpochMillis(Instant instant) {
        return Optional.ofNullable(instant).map(Instant::toEpochMilli).orElse(null);
    }
}
