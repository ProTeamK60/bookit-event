package se.knowit.bookitevent.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitevent.model.Event;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceMapImplTest {
    private static final Long DEFAULT_ID = 1L;
    private static final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
    private static final Event DEFAULT_EVENT = buildDefaultEvent();
    
    private static Event buildDefaultEvent() {
        LocalDateTime startTime = LocalDateTime.of(2019, 11, 13, 17, 56);
        Event event = new Event();
        event.setName("DEFAULT_EVENT");
        event.setId(DEFAULT_ID);
        event.setEventId(DEFAULT_UUID);
        event.setEventStart(startTime);
        event.setEventEnd(startTime.plusHours(2));
        event.setDeadlineRVSP(startTime.minusDays(2));
        event.setLocation("K60");
        event.setOrganizer("Erik");
        event.setDescription("Ett himla bra event!");
        return event;
    }
    
    private EventServiceMapImpl eventService;
    
    private Map<Long, Event> container;
    
    @BeforeEach
    void setUp() {
        container = new ConcurrentHashMap<>();
        eventService = new EventServiceMapImpl(container);
        eventService.save(DEFAULT_EVENT);
    }
    
    @Test
    void firstGeneratedIdShouldBe_1() {
        Event event = getValidTestEvent();
        
        //Ensure there are nothing stored in the map
        container.clear();
        Event returnedEvent = eventService.save(event);
        assertEquals(1L, returnedEvent.getId());
    }
    
    @Test
    void savingEventWithoutStartTimeShouldThrowException() {
        Event event = new Event();
        event.setName("Invalid Event");
        assertThrows(IllegalArgumentException.class, () -> eventService.save(event));
    }
    
    @Test
    void saveNewEventShouldReturnEventWithIdsSet() {
        Event event = getValidTestEvent();
        
        assertNull(event.getId());
        assertNull(event.getEventId());
        
        Event returnedEvent = eventService.save(event);
        
        assertNotNull(returnedEvent);
        assertNotNull(returnedEvent.getId());
        assertNotNull(returnedEvent.getEventId());
    }
    
    @Test
    void savingAnExistingEventMustNotChangeItsId() {
        Event event = getValidTestEvent();
        event.setId(10L);
        Event returnedEvent = eventService.save(event);
        assertEquals(event.getId(), returnedEvent.getId());
    }
    
    private Event getValidTestEvent() {
        Event event = new Event();
        event.setName("Test event");
        event.setEventStart(LocalDateTime.now());
        return event;
    }
    
    @Test
    void itShouldNotBePossibleToSaveANullObject() {
        assertThrows(NullPointerException.class, () -> eventService.save(null));
    }
    
    @Test
    void itShouldNotBePossibleToSaveEventWithoutNameAndStartTime() {
        assertThrows(IllegalArgumentException.class, () -> eventService.save(new Event()));
    }
    
    @Test
    void itShouldBePossibleToGetEventByInternalId() {
        Optional<Event> event = eventService.findById(DEFAULT_ID);
        assertTrue(event.isPresent(), "Event should be present in returned Optional");
    }
    
    @Test
    void itShouldBePossibleToFindEventByExternalId() {
        Optional<Event> optionalEvent = eventService.findByEventId(DEFAULT_UUID);
        assertTrue(optionalEvent.isPresent());
    }
    
    @Test
    void allEventsShouldBeReturnedByFindAll() {
        Set<Event> events = eventService.findAll();
        assertEquals(container.size(), events.size());
    }
}
