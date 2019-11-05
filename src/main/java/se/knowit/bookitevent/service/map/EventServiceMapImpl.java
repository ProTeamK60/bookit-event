package se.knowit.bookitevent.service.map;

import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventServiceMapImpl implements EventService {
	private final Map<Long, Event> map;
	
	public EventServiceMapImpl() {
		this(new ConcurrentHashMap<>());
	}
	
	EventServiceMapImpl(Map<Long, Event> map) {
		this.map = map;
	}
	
	@Override
	public Optional<Event> findById(Long id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public Event save(Event incomingEvent) {
		Event event = ensureEventIsValidOrThrowException(incomingEvent);
		assignRequiredIds(event);
		map.put(event.getId(), event);
		return event;
	}
	
	private void assignRequiredIds(Event event) {
		assignPersistenceIdIfNotSet(event);
		assignEventIdIfNotSet(event);
	}
	
	private void assignEventIdIfNotSet(Event event) {
		if (event.getEventId() == null) {
			event.setEventId(UUID.randomUUID());
		}
	}
	
	private void assignPersistenceIdIfNotSet(Event event) {
		if (event.getId() == null) {
			event.setId(getNextId());
		}
	}
	
	private Event ensureEventIsValidOrThrowException(Event incomingEvent) {
		Event event = Objects.requireNonNull(incomingEvent, "Event argument must not be null");
		ensureNameIsSet(event);
		ensureStartTimeIsSet(event);
		return event;
	}
	
	private void ensureStartTimeIsSet(Event event) {
		if (event.getEventStart() == null){
			throw new IllegalArgumentException("Event must have a start time");
		}
	}
	
	private void ensureNameIsSet(Event event) {
		if (event.getName() == null || event.getName().isBlank()) {
			throw new IllegalArgumentException("Event must have a name");
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
