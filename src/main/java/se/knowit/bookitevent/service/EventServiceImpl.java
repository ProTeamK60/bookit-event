package se.knowit.bookitevent.service;

import org.springframework.stereotype.Service;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.repository.EventRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static se.knowit.bookitevent.service.EventService.Outcome.CREATED;
import static se.knowit.bookitevent.service.EventService.Outcome.UPDATED;

@Service
public class EventServiceImpl implements EventService {
    
    private final EventRepository eventRepository;
    private final EventMapper mapper;
    
    public EventServiceImpl(final EventRepository eventRepository) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        mapper = new EventMapper();
    }
    
    @Override
    public CreateOrUpdateCommandResult createOrUpdate(EventDTO eventDTO) {
        Event event = mapper.fromDTO(eventDTO);
        Outcome outcome = event.getEventId() == null ? CREATED: UPDATED;
        
        try {
            Event saved = eventRepository.save(event);
            return new CreateOrUpdateCommandResult(outcome, saved.getEventId());
        } catch (RuntimeException e) {
            return new CreateOrUpdateCommandResult(e);
        }
    }
    
    @Override
    public Set<Event> findAll() {
        return eventRepository.findAll();
    }
    
    @Override
    public Optional<Event> findByEventId(UUID eventId) {
        return eventRepository.findByEventId(eventId);
    }
}
