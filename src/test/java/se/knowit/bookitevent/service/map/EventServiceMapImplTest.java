package se.knowit.bookitevent.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.knowit.bookitevent.model.Event;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventServiceMapImplTest {
    
    private EventServiceMapImpl eventService;
    
    @BeforeEach
    void setUp() {
        eventService = new EventServiceMapImpl();
    }
    
    @Test
    void saveNewEventShouldReturnEventWithId() {
        Event event = new Event();
        event.setName("Test");
    
        Event returnedEvent = eventService.save(event);
        assertNotNull(returnedEvent);
        assertNotNull(returnedEvent.getId());
    }
}
