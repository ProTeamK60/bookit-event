package se.knowit.bookitevent.service.map;

import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.model.EventValidator;
import se.knowit.bookitevent.service.EventService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventServiceMapImpl implements EventService {
	private final Map<Long, Event> map;
	private final EventValidator eventValidator;
	
	public EventServiceMapImpl() {
		this(new ConcurrentHashMap<>());
	}
	
	EventServiceMapImpl(Map<Long, Event> map) {
		this.map = map;
		eventValidator = new EventValidator();
	}
	
	@Override
	public Optional<Event> findById(Long id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public Event save(Event incomingEvent) {
		Event event = eventValidator.ensureEventIsValidOrThrowException(incomingEvent);
		assignRequiredIds(event);
		map.put(event.getId(), event);
		return event;
	}
	
	private void assignRequiredIds(Event event) {
		assignPersistenceIdIfNotSet(event);
		assignEventIdIfNotSet(event);
	}
	private void assignPersistenceIdIfNotSet(Event event) {
		if (event.getId() == null) {
			event.setId(getNextId());
		}
	}
	
	private void assignEventIdIfNotSet(Event event) {
		if (event.getEventId() == null) {
			event.setEventId(UUID.randomUUID());
		}
	}
	
	
	private Long getNextId() {
		try {
			return Collections.max(map.keySet()) + 1L;
		} catch (NoSuchElementException ignored) {
			return 1L;
		}
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
}
