package se.knowit.bookitevent.repository.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.model.Event;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class EventRepositoryMapImplTest {
    private static final Long DEFAULT_ID = 1L;
    private static final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
    private static final Event DEFAULT_EVENT = buildDefaultEvent();
    
    private static Event buildDefaultEvent() {
        Instant startTime = ZonedDateTime.of(2019, 11, 13, 17, 56,0,0, ZoneId.systemDefault()).toInstant();
        Event event = new Event();
        event.setName("DEFAULT_EVENT");
        event.setId(DEFAULT_ID);
        event.setEventId(DEFAULT_UUID);
        event.setEventStart(startTime);
        event.setEventEnd(startTime.plus(2, HOURS));
        event.setDeadlineRVSP(startTime.minus(2, DAYS));
        event.setLocation("K60");
        event.setOrganizer("Erik");
        event.setDescription("Ett himla bra event!");
        return event;
    }
    
    @Mock
    private KafkaProducerService<String, EventDTO> kafkaProducerService;
    private EventRepositoryMapImpl eventRepository;
    
    private Map<Long, Event> container;
    
    @BeforeEach
    void setUp() {
        container = new ConcurrentHashMap<>();
        container.put(DEFAULT_ID, DEFAULT_EVENT);
        eventRepository = new EventRepositoryMapImpl(kafkaProducerService, container);
    }
    
    @Test
    void firstGeneratedIdShouldBe_1() {
        Event event = getValidTestEvent();
        
        //Ensure there are nothing stored in the map
        container.clear();
        Event returnedEvent = eventRepository.save(event);
        assertEquals(1L, returnedEvent.getId());
        verifyPublished(returnedEvent);
    }
    
    private void verifyPublished(Event event) {
        EventDTO eventDTO = new EventMapper().toDTO(event);
        verify(kafkaProducerService).sendMessage("events", eventDTO.getEventId(), eventDTO);
    }
    
    @Test
    void savingEventWithoutStartTimeShouldThrowException() {
        Event event = new Event();
        event.setName("Invalid Event");
        assertThrows(IllegalArgumentException.class, () -> eventRepository.save(event));
        verifyNoInteractions(kafkaProducerService);
    }
    
    @Test
    void saveNewEventShouldReturnEventWithIdsSet() {
        Event event = getValidTestEvent();
        
        assertNull(event.getId());
        assertNull(event.getEventId());
        
        Event returnedEvent = eventRepository.save(event);
        
        assertNotNull(returnedEvent);
        assertNotNull(returnedEvent.getId());
        assertNotNull(returnedEvent.getEventId());
        verifyPublished(returnedEvent);
    }
    
    @Test
    void savingAnExistingEventMustNotChangeItsId() {
        Event event = getValidTestEvent();
        event.setId(10L);
        Event returnedEvent = eventRepository.save(event);
        assertEquals(event.getId(), returnedEvent.getId());
        verifyPublished(returnedEvent);
    }
    
    private Event getValidTestEvent() {
        Event event = new Event();
        event.setName("Test event");
        event.setEventStart(Instant.now());
        return event;
    }
    
    @Test
    void itShouldNotBePossibleToSaveANullObject() {
        assertThrows(NullPointerException.class, () -> eventRepository.save(null));
    }
    
    @Test
    void itShouldNotBePossibleToSaveEventWithoutNameAndStartTime() {
        assertThrows(IllegalArgumentException.class, () -> eventRepository.save(new Event()));
    }
    
    @Test
    void itShouldBePossibleToGetEventByInternalId() {
        Optional<Event> event = eventRepository.findById(DEFAULT_ID);
        assertTrue(event.isPresent(), "Event should be present in returned Optional");
    }
    
    @Test
    void itShouldBePossibleToFindEventByExternalId() {
        Optional<Event> optionalEvent = eventRepository.findByEventId(DEFAULT_UUID);
        assertTrue(optionalEvent.isPresent());
    }
    
    @Test
    void allEventsShouldBeReturnedByFindAll() {
        Set<Event> events = eventRepository.findAll();
        assertEquals(container.size(), events.size());
    }
}
