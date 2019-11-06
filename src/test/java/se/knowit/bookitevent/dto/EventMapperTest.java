package se.knowit.bookitevent.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitevent.model.Event;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventMapperTest {
    
    private EventDTO dtoFixture;
    private Event eventFixture;
    
    @BeforeEach
    void setUp() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("A test event");
        eventDTO.setDescription("Test description");
        eventDTO.setEventStart("1970-01-02T01:00:00");
        eventDTO.setEventEnd("1970-01-02T01:01:00");
        eventDTO.setDeadlineRVSP("1970-01-01T18:00:00");
        eventDTO.setLocation("K60");
        eventDTO.setOrganizer("Ola");
        dtoFixture = eventDTO;
        Event event = new Event();
        event.setName("A test event");
        event.setDescription("Test description");
        event.setEventStart(LocalDateTime.parse("1970-01-02T01:00:00"));
        event.setEventEnd(LocalDateTime.parse("1970-01-02T01:01:00"));
        event.setDeadlineRVSP(LocalDateTime.parse("1970-01-01T18:00:00"));
        event.setLocation("K60");
        event.setOrganizer("Ola");
        eventFixture = event;
    }
    
    @Test
    void eventDtoCanBeConvertedToEventImplementation() {
        EventMapper mapper = new EventMapper();
        Event event = mapper.fromDTO(dtoFixture);
        assertEquals(dtoFixture.getName(), event.getName());
        assertEquals(dtoFixture.getDescription(), event.getDescription());
        assertEquals(LocalDateTime.parse(dtoFixture.getEventStart()), event.getEventStart());
        assertEquals(LocalDateTime.parse(dtoFixture.getEventEnd()), event.getEventEnd());
        assertEquals(LocalDateTime.parse(dtoFixture.getDeadlineRVSP()), event.getDeadlineRVSP());
        assertEquals(dtoFixture.getLocation(), event.getLocation());
        assertEquals(dtoFixture.getOrganizer(), event.getOrganizer());
    }
    
    @Test
    void eventInstanceCanBeConvertedToDto() {
        EventMapper mapper = new EventMapper();
        EventDTO dto = mapper.toDTO(eventFixture);
        assertEquals(eventFixture.getName(), dto.getName());
        assertEquals(eventFixture.getDescription(), dto.getDescription());
        assertEquals(eventFixture.getEventStart(), LocalDateTime.parse(dto.getEventStart()));
        assertEquals(eventFixture.getEventEnd(), LocalDateTime.parse(dto.getEventEnd()));
        assertEquals(eventFixture.getDeadlineRVSP(), LocalDateTime.parse(dto.getDeadlineRVSP()));
        assertEquals(eventFixture.getLocation(), dto.getLocation());
        assertEquals(eventFixture.getOrganizer(), dto.getOrganizer());
    }
}
