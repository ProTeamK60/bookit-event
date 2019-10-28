package se.knowit.bookitevent.service.map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Profile("map")
public class EventServiceMapImpl implements EventService {
    private Map<Long, Event> map = new HashMap<>();
    
    @Override
    public Event findById(Long aLong) {
        return null;
    }
    
    @Override
    public Event save(Event object) {
        if (object != null) {
            if (object.getId() == null) {
                object.setId(getNextId());
            }
            map.put(object.getId(), object);
        } else {
            throw new IllegalArgumentException("object must not be null");
        }
        return object;
    }
    
    private Long getNextId() {
        try {
            return Collections.max(map.keySet()) + 1L;
        } catch (NoSuchElementException ignored) {
            return 1L;
        }
    }
}
