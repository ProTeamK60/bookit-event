package se.knowit.bookitevent.dto;

import lombok.Data;

@Data
public class EventDTO {
    private String eventId;
    private String description;
    private String name;
    private String eventStart;
    private String eventEnd;
    private String deadlineRVSP;
    private String location;
    private String organizer;
}
