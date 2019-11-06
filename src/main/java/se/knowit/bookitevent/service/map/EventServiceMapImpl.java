package se.knowit.bookitevent.service.map;

import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.model.EventValidator;
import se.knowit.bookitevent.service.EventService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventServiceMapImpl implements EventService {
    private final Map<Long, Event> map;
    private final EventValidator eventValidator;
    private final IdentityHandler identityHandler;
    
    public EventServiceMapImpl() {
        this(new ConcurrentHashMap<>());
    }
    
    EventServiceMapImpl(Map<Long, Event> map) {
        this.map = map;
        eventValidator = new EventValidator();
        identityHandler = new IdentityHandler();
    }
    
    @Override
    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }
    
    @Override
    public Event save(Event incomingEvent) {
        Event event = eventValidator.ensureEventIsValidOrThrowException(incomingEvent);
        assignRequiredIds(event);
        persistEvent(event);
        return event;
    }
    
    
    private void assignRequiredIds(Event event) {
        identityHandler.assignPersistenceIdIfNotSet(event, this);
        identityHandler.assignEventIdIfNotSet(event);
    }
    
    private void persistEvent(Event event) {
        map.put(event.getId(), event);
    }
	
	
	@Override
    public Optional<Event> findByEventId(UUID id) {
        return map.values().stream()
                .filter(ev -> ev.haveEventId(id))
                .findFirst();
    }
    
    @Override
    public Set<Event> findAll() {
        return Set.copyOf(map.values());
    }
    
    private static class IdentityHandler {
	
		void assignPersistenceIdIfNotSet(Event event, EventServiceMapImpl eventServiceMap) {
			if (event.getId() == null) {
				event.setId(getNextId(eventServiceMap));
			}
		}
	
		void assignEventIdIfNotSet(Event event) {
			if (event.getEventId() == null) {
				event.setEventId(UUID.randomUUID());
			}
		}
	
		Long getNextId(EventServiceMapImpl eventServiceMap) {
			try {
				return Collections.max(eventServiceMap.map.keySet()) + 1L;
			} catch (NoSuchElementException ignored) {
				return 1L;
			}
		}
	}
}
