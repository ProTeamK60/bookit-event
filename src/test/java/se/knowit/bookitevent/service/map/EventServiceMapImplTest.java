package se.knowit.bookitevent.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitevent.model.Event;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceMapImplTest {
    
    private static final Long DEFAULT_ID = 1L;
    private EventServiceMapImpl eventService;
    
    @BeforeEach
    void setUp() {
        eventService = new EventServiceMapImpl();
        Event event = new Event();
        event.setName("DEFAULT_EVENT");
        event.setId(DEFAULT_ID);
        event.setEventId("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
        eventService.save(event);
    }
    
    @Test
    void saveNewEventShouldReturnEventWithId() {
        Event event = new Event();
        event.setName("Test");
    
        Event returnedEvent = eventService.save(event);
        assertNotNull(returnedEvent);
        assertNotNull(returnedEvent.getId());
    }
    
    @Test
    void savingAnExistingEventMustNotChangeItsId() {
        Event event = new Event();
        event.setId(10L);
        Event returnedEvent = eventService.save(event);
        assertEquals(event.getId(), returnedEvent.getId());
    }
    
    @Test
    void itShouldNotBePossibleToSaveANullObject() {
        assertThrows(IllegalArgumentException.class, () -> eventService.save(null));
    }
    
    @Test
    void itShouldBePossibleToGetEventByInternalId() {
        Event event = eventService.findById(DEFAULT_ID);
        assertNotNull(event);
    }
}
