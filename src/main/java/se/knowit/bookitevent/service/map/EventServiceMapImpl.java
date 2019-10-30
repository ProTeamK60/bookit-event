package se.knowit.bookitevent.service.map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("map")
public class EventServiceMapImpl implements EventService {
	private final Map<Long, Event> map;
	
	public EventServiceMapImpl() {
		this(new ConcurrentHashMap<>());
	}
	
	public EventServiceMapImpl(Map<Long, Event> map) {
		this.map = map;
	}
	
	@Override
	public Event findById(Long aLong) {
		return map.get(aLong);
	}

	@Override
	public Event save(Event object) {
		Event event = Objects.requireNonNull(object, "object must not be null");
		
		if (event.getId() == null) {
			event.setId(getNextId());
		}
		map.put(event.getId(), event);
		return event;
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
		Optional<Event> event = map.values().stream()
				.filter(ev -> ev.haveEventId(id))
				.findFirst();
		return event;
	}

	@Override
	public Set<Event> findAll() {
		return Set.copyOf(map.values());
	}
}
