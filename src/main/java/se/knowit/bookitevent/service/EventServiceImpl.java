package se.knowit.bookitevent.service;

import org.springframework.stereotype.Service;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.repository.EventRepository;

import java.util.Objects;

import static se.knowit.bookitevent.service.EventService.Outcome.CREATED;
import static se.knowit.bookitevent.service.EventService.Outcome.UPDATED;

@Service
public class EventServiceImpl implements EventService {
    
    private final EventRepository eventRepository;
    private final KafkaProducerService<String, EventDTO> kafkaService;
    private final EventMapper mapper;
    
    public EventServiceImpl(final EventRepository eventRepository, final KafkaProducerService<String, EventDTO> kafkaService) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.kafkaService = Objects.requireNonNull(kafkaService);
        mapper = new EventMapper();
    }
    
    @Override
    public CreateOrUpdateCommandResult createOrUpdate(EventDTO eventDTO) {
        Event event = mapper.fromDTO(eventDTO);
        Outcome outcome = event.getEventId() == null ? CREATED: UPDATED;
        
        try {
            Event saved = eventRepository.save(event);
            EventDTO savedDTO = mapper.toDTO(saved);
            kafkaService.sendMessage("events", savedDTO.getEventId(), savedDTO);
            return new CreateOrUpdateCommandResult(outcome, saved.getEventId());
        } catch (RuntimeException e) {
            return new CreateOrUpdateCommandResult(e);
        }
    }
}
