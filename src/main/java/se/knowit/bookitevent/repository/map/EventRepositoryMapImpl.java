package se.knowit.bookitevent.repository.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.model.EventStatus;
import se.knowit.bookitevent.model.EventValidator;
import se.knowit.bookitevent.repository.EventRepository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EventRepositoryMapImpl implements EventRepository {
    private final KafkaProducerService<String, EventDTO> kafkaProducerService;
    private final Map<UUID, Event> map;
    private final EventValidator eventValidator;
    private final IdentityHandler identityHandler;
    private final EventMapper mapper;
    
    @Autowired
    public EventRepositoryMapImpl(KafkaProducerService<String, EventDTO> kafkaProducerService) {
        this(kafkaProducerService, new ConcurrentHashMap<>());
    }
    
    EventRepositoryMapImpl(KafkaProducerService<String, EventDTO> kafkaProducerService, Map<UUID, Event> map) {
        this.kafkaProducerService = kafkaProducerService;
        this.map = map;
        eventValidator = new EventValidator();
        identityHandler = new IdentityHandler();
        mapper = new EventMapper();
    }
    
    @Override
    public Event save(Event incomingEvent) {
        Event event = eventValidator.ensureEventIsValidOrThrowException(incomingEvent);
        assignRequiredIds(event);
        Event oldEvent = persistEvent(event);
        if (eventsAreDifferent(event, oldEvent)) {
            publish(event);
        }
        return event;
    }

    private boolean eventsAreDifferent(Event event, Event oldEvent) {
        return !event.equals(oldEvent);
    }
    
    private void publish(Event event) {
        EventDTO eventDTO = mapper.toDTO(event);
        kafkaProducerService.sendMessage("events", eventDTO.getEventId(), eventDTO);
    }
    
    
    private void assignRequiredIds(Event event) {
        identityHandler.assignEventIdIfNotSet(event);
    }
    
    private Event persistEvent(Event event) {
        return map.put(event.getEventId(), event);
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

    @Override
    /**
     * @UUID uuid input derived from String
     */
    public Optional<Event> markDeletedByEventId(UUID id)
    {
        Event event = map.get(id);
        if (null!= event){
            event.setStatus(EventStatus.DELETED.toString());
            map.put(id, event);
            System.out.println(map.get(event.getEventId()).toString());
            this.publish(event);
        }
        // return eventRepository.markDeletedByEventId(id);
        return Optional.of(event);
    }

    private static class IdentityHandler {
	
		void assignEventIdIfNotSet(Event event) {
			if (event.getEventId() == null) {
				event.setEventId(UUID.randomUUID());
			}
		}
	}
}
