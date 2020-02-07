package se.knowit.bookitevent.service;

import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.model.Event;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EventService {
    
    CreateOrUpdateCommandResult createOrUpdate(EventDTO event);
    
    Set<Event> findAll();
    
    Optional<Event> findByEventId(UUID id);

    Optional<Event> deleteByEventId(UUID fromString);

    enum Outcome {CREATED, UPDATED, FAILED, NO_CHANGE}
    
    class CreateOrUpdateCommandResult {
        private final Outcome outcome;
        private final UUID eventId;
        private final Throwable throwable;
        
        public CreateOrUpdateCommandResult(Throwable throwable) {
            this(Outcome.FAILED, null, throwable);
        }
        
        public CreateOrUpdateCommandResult(Outcome outcome, UUID eventId) {
            this(outcome, eventId, null);
        }
        
        private CreateOrUpdateCommandResult(Outcome outcome, UUID eventId, Throwable throwable) {
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
}
