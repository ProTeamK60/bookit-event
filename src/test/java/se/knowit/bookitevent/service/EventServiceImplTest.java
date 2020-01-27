package se.knowit.bookitevent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.repository.EventRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    private static final String EVENT_ID = "ea4ab6c0-8a73-4e9b-b28a-7bb9e0f87b18";
    @Mock
    private EventRepository service;

    @Mock
    private KafkaProducerService<String, EventDTO> kafkaService;

    @InjectMocks
    private EventServiceImpl command;
    
    private final Event invalidNewEvent = new Event();
    private final EventDTO invalidNewDto = new EventDTO();
    
    private Event validNewEvent;
    private EventDTO validNewDTO;
    
    private Event forUpdateEvent;
    private EventDTO forUpdateDto;
    
    @BeforeEach
    void setUp() {
        EventMapper mapper = new EventMapper();
        validNewDTO = new EventDTO();
        validNewDTO.setName("A test event");
        validNewDTO.setEventStart(Instant.parse("1970-01-02T01:00:00.000Z").toEpochMilli());
        validNewEvent = mapper.fromDTO(validNewDTO);
    
        forUpdateDto = new EventDTO();
        forUpdateDto.setName("A test event");
        forUpdateDto.setEventStart(Instant.parse("1970-01-02T01:00:00.000Z").toEpochMilli());
        forUpdateDto.setEventId(EVENT_ID);
        forUpdateEvent = mapper.fromDTO(forUpdateDto);
    }
    
    @Test
    void savingANewValidEventShouldReturnOutcomeCreated() {
        when(service.save(eq(validNewEvent))).thenReturn(forUpdateEvent);
        EventServiceImpl.CommandResult result = command.apply(validNewDTO);
        assertEquals(EventServiceImpl.Outcome.CREATED, result.getOutcome());
        assertTrue(result.getEventId().isPresent());
        assertEquals(forUpdateEvent.getEventId(), result.getEventId().get());
    }
    
    @Test
    void updatingExistingEventShouldReturnOutcomeUpdated() {
        when(service.save(eq(forUpdateEvent))).thenReturn(forUpdateEvent);
        EventServiceImpl.CommandResult result = command.apply(forUpdateDto);
        assertEquals(EventServiceImpl.Outcome.UPDATED, result.getOutcome());
        assertTrue(result.getEventId().isPresent());
        assertEquals(forUpdateEvent.getEventId(), result.getEventId().get());
    }
    
    @Test
    void savingInvalidEventShouldReturnOutcomeFailed() {
        when(service.save(eq(invalidNewEvent))).thenThrow(IllegalArgumentException.class);
        EventServiceImpl.CommandResult result = command.apply(invalidNewDto);
        assertEquals(EventServiceImpl.Outcome.FAILED, result.getOutcome());
        assertTrue(result.getEventId().isEmpty());
        assertTrue(result.getThrowable().isPresent());
        assertEquals(IllegalArgumentException.class, result.getThrowable().get().getClass());
    }
}
