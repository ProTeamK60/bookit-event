package se.knowit.bookitevent.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitevent.model.Event;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventMapperTest {
    
    private EventDTO dtoFixture;
    private Event eventFixture;
    
    @BeforeEach
    void setUp() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("A test event");
        eventDTO.setDescription("Test description");
        eventDTO.setEventStart("1970-01-02T01:00:00.000Z");
        eventDTO.setEventEnd("1970-01-02T01:01:00.000Z");
        eventDTO.setDeadlineRVSP("1970-01-01T18:00:00.000Z");
        eventDTO.setLocation("K60");
        eventDTO.setOrganizer("Ola");
        dtoFixture = eventDTO;
        Event event = new Event();
        event.setName("A test event");
        event.setDescription("Test description");
        event.setEventStart(parseTime("1970-01-02T01:00:00.000Z"));
        event.setEventEnd(parseTime("1970-01-02T01:01:00.000Z"));
        event.setDeadlineRVSP(parseTime("1970-01-01T18:00:00.000Z"));
        event.setLocation("K60");
        event.setOrganizer("Ola");
        eventFixture = event;
    }
    
    private ZonedDateTime parseTime(String eventStart) {
        return OffsetDateTime.parse(eventStart).toZonedDateTime();
    }
    
    @Test
    void eventDtoCanBeConvertedToEventImplementation() {
        EventMapper mapper = new EventMapper();
        Event event = mapper.fromDTO(dtoFixture);
        assertEquals(dtoFixture.getName(), event.getName());
        assertEquals(dtoFixture.getDescription(), event.getDescription());
        assertEquals(parseTime(dtoFixture.getEventStart()), event.getEventStart());
        assertEquals(parseTime(dtoFixture.getEventEnd()), event.getEventEnd());
        assertEquals(parseTime(dtoFixture.getDeadlineRVSP()), event.getDeadlineRVSP());
        assertEquals(dtoFixture.getLocation(), event.getLocation());
        assertEquals(dtoFixture.getOrganizer(), event.getOrganizer());
    }
    
    @Test
    void eventInstanceCanBeConvertedToDto() {
        EventMapper mapper = new EventMapper();
        EventDTO dto = mapper.toDTO(eventFixture);
        assertEquals(eventFixture.getName(), dto.getName());
        assertEquals(eventFixture.getDescription(), dto.getDescription());
        assertEquals(eventFixture.getEventStart(), parseTime(dto.getEventStart()));
        assertEquals(eventFixture.getEventEnd(), parseTime(dto.getEventEnd()));
        assertEquals(eventFixture.getDeadlineRVSP(), parseTime(dto.getDeadlineRVSP()));
        assertEquals(eventFixture.getLocation(), dto.getLocation());
        assertEquals(eventFixture.getOrganizer(), dto.getOrganizer());
    }
}
