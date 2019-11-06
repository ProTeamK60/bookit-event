package se.knowit.bookitevent.service;

import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class CreateOrUpdateCommand implements Function<EventDTO, CreateOrUpdateCommand.CommandResult> {
    
    public enum Outcome {CREATED, UPDATED, FAILED}
    
    public static class CommandResult {
        private final Outcome outcome;
        private final UUID eventId;
        private final Throwable throwable;
        
        CommandResult(Throwable throwable) {
            this(Outcome.FAILED, null, throwable);
        }
        
        CommandResult(Outcome outcome, UUID eventId) {
            this(outcome, eventId, null);
        }
        
        CommandResult(Outcome outcome, UUID eventId, Throwable throwable) {
            this.outcome = Objects.requireNonNull(outcome);
            this.eventId = eventId;
            this.throwable = throwable;
        }
        
        public Outcome getOutcome() {
            return outcome;
        }
        
        public Optional<UUID> getEventId() {
            return Optional.ofNullable(eventId);
        }
        
        public Optional<Throwable> getThrowable() {
            return Optional.ofNullable(throwable);
        }
    }
    
    private final EventService service;
    
    public CreateOrUpdateCommand(EventService service) {
        this.service = Objects.requireNonNull(service);
    }
    
    @Override
    public CommandResult apply(EventDTO eventDTO) {
        EventMapper mapper = new EventMapper();
        Event event = mapper.fromDTO(eventDTO);
        Outcome shouldBe = null;
        if (event.getEventId() == null) {
            shouldBe = Outcome.CREATED;
        }
        if (event.getEventId() != null) {
            shouldBe = Outcome.UPDATED;
        }
        try {
            Event saved = service.save(event);
            return new CommandResult(shouldBe, saved.getEventId());
        } catch (RuntimeException e) {
            return new CommandResult(e);
        }
    }
}
