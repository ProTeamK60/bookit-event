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
import se.knowit.bookitevent.repository.EventRepository;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static se.knowit.bookitevent.service.EventService.Outcome.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    private static final String EVENT_ID = "ea4ab6c0-8a73-4e9b-b28a-7bb9e0f87b18";
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;
    
    private final Event invalidNewEvent = new Event();
    private final EventDTO invalidNewDto = new EventDTO();
    
    private Event validNewEvent;
    private EventDTO validNewDTO;
    
    private Event forUpdateEvent;
    private EventDTO forUpdateDto;
    private EventDTO forUpdateWithChangeDto;
    private Event forUpdateWithChangeEvent;
    
    @BeforeEach
    void setUp() {
        EventMapper mapper = new EventMapper();
        validNewDTO = new EventDTO();
        validNewDTO.setName("A test event");
        validNewDTO.setEventStart(Instant.parse("1970-01-02T01:00:00.000Z").toEpochMilli());
        validNewEvent = mapper.fromDTO(validNewDTO);
    
        forUpdateDto = new EventDTO();
        forUpdateDto.setName(validNewDTO.getName());
        forUpdateDto.setEventStart(validNewDTO.getEventStart());
        forUpdateDto.setEventId(EVENT_ID);
        forUpdateEvent = mapper.fromDTO(forUpdateDto);
        
        forUpdateWithChangeDto = new EventDTO();
        forUpdateWithChangeDto.setName(forUpdateDto.getName());
        forUpdateWithChangeDto.setEventStart(forUpdateDto.getEventStart());
        forUpdateWithChangeDto.setDeadlineRVSP(Instant.parse("1970-01-02T00:59:59.000Z").toEpochMilli());
        forUpdateWithChangeDto.setEventId(forUpdateDto.getEventId());
        forUpdateWithChangeEvent = mapper.fromDTO(forUpdateWithChangeDto);
    }
    
    @Test
    void savingANewValidEventShouldReturnOutcomeCreated() {
        when(eventRepository.save(eq(validNewEvent))).thenReturn(forUpdateEvent);
        var result = eventService.createOrUpdate(validNewDTO);
        assertEquals(CREATED, result.getOutcome());
        assertTrue(result.getEventId().isPresent());
        assertEquals(forUpdateEvent.getEventId(), result.getEventId().get());
    }
    
    @Test
    void updatingExistingEventWithChangesShouldReturnOutcomeUpdated() {
        when(eventRepository.findByEventId(eq(forUpdateWithChangeEvent.getEventId()))).thenReturn(Optional.of(forUpdateEvent));
        when(eventRepository.save(eq(forUpdateWithChangeEvent))).thenReturn(forUpdateWithChangeEvent);
        
        var result = eventService.createOrUpdate(forUpdateWithChangeDto);
        assertEquals(UPDATED, result.getOutcome());
        assertTrue(result.getEventId().isPresent());
        assertEquals(forUpdateWithChangeEvent.getEventId(), result.getEventId().get());
    }
    
    @Test
    void attemptToSaveExistingEventWithoutChangesShouldReturnOutcomeNoChange() {
        when(eventRepository.findByEventId(eq(forUpdateEvent.getEventId()))).thenReturn(Optional.of(forUpdateEvent));
        
        var result = eventService.createOrUpdate(forUpdateDto);
        assertEquals(NO_CHANGE, result.getOutcome());
        assertTrue(result.getEventId().isPresent());
        assertEquals(forUpdateEvent.getEventId(), result.getEventId().get());
    }
    
    @Test
    void savingInvalidEventShouldReturnOutcomeFailed() {
        when(eventRepository.save(eq(invalidNewEvent))).thenThrow(IllegalArgumentException.class);
        var result = eventService.createOrUpdate(invalidNewDto);
        assertEquals(FAILED, result.getOutcome());
        assertTrue(result.getEventId().isEmpty());
        assertTrue(result.getThrowable().isPresent());
        assertEquals(IllegalArgumentException.class, result.getThrowable().get().getClass());
    }
}
