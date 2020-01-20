package se.knowit.bookitevent.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrUpdateCommandTest {
    private static final String EVENT_ID = "ea4ab6c0-8a73-4e9b-b28a-7bb9e0f87b18";
    @Mock
    private EventService service;

    @Mock
    private KafkaService kafkaService;

    @InjectMocks
    private CreateOrUpdateCommand command;
    
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
        CreateOrUpdateCommand.CommandResult result = command.apply(validNewDTO);
        assertEquals(CreateOrUpdateCommand.Outcome.CREATED, result.getOutcome());
        assertTrue(result.getEventId().isPresent());
        assertEquals(forUpdateEvent.getEventId(), result.getEventId().get());
    }
    
    @Test
    void updatingExistingEventShouldReturnOutcomeUpdated() {
        when(service.save(eq(forUpdateEvent))).thenReturn(forUpdateEvent);
        CreateOrUpdateCommand.CommandResult result = command.apply(forUpdateDto);
        assertEquals(CreateOrUpdateCommand.Outcome.UPDATED, result.getOutcome());
        assertTrue(result.getEventId().isPresent());
        assertEquals(forUpdateEvent.getEventId(), result.getEventId().get());
    }
    
    @Test
    void savingInvalidEventShouldReturnOutcomeFailed() {
        when(service.save(eq(invalidNewEvent))).thenThrow(IllegalArgumentException.class);
        CreateOrUpdateCommand.CommandResult result = command.apply(invalidNewDto);
        assertEquals(CreateOrUpdateCommand.Outcome.FAILED, result.getOutcome());
        assertTrue(result.getEventId().isEmpty());
        assertTrue(result.getThrowable().isPresent());
        assertEquals(IllegalArgumentException.class, result.getThrowable().get().getClass());
    }
}
