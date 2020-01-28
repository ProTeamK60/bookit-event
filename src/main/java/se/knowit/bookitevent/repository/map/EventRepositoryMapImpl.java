package se.knowit.bookitevent.repository.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.model.EventValidator;
import se.knowit.bookitevent.repository.EventRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EventRepositoryMapImpl implements EventRepository {
    private final KafkaProducerService<String, EventDTO> kafkaProducerService;
    private final Map<Long, Event> map;
    private final EventValidator eventValidator;
    private final IdentityHandler identityHandler;
    private final EventMapper mapper;
    
    @Autowired
    public EventRepositoryMapImpl(KafkaProducerService<String, EventDTO> kafkaProducerService) {
        this(kafkaProducerService, new ConcurrentHashMap<>());
    }
    
    EventRepositoryMapImpl(KafkaProducerService<String, EventDTO> kafkaProducerService, Map<Long, Event> map) {
        this.kafkaProducerService = kafkaProducerService;
        this.map = map;
        eventValidator = new EventValidator();
        identityHandler = new IdentityHandler();
        mapper = new EventMapper();
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
        publish(event);
        return event;
    }
    
    private void publish(Event event) {
        EventDTO eventDTO = mapper.toDTO(event);
        kafkaProducerService.sendMessage("events", eventDTO.getEventId(), eventDTO);
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
	
		void assignPersistenceIdIfNotSet(Event event, EventRepositoryMapImpl eventServiceMap) {
			if (event.getId() == null) {
				event.setId(getNextId(eventServiceMap));
			}
		}
	
		void assignEventIdIfNotSet(Event event) {
			if (event.getEventId() == null) {
				event.setEventId(UUID.randomUUID());
			}
		}
	
		Long getNextId(EventRepositoryMapImpl eventServiceMap) {
			try {
				return Collections.max(eventServiceMap.map.keySet()) + 1L;
			} catch (NoSuchElementException ignored) {
				return 1L;
			}
		}
	}
}
