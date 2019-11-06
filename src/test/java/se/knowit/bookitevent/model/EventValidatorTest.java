package se.knowit.bookitevent.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventValidatorTest {
    private final EventValidator eventValidator = new EventValidator();
    
    @Test
    void ensureEventWithMinimalDataIsValid() {
        assertDoesNotThrow(() -> eventValidator.ensureEventIsValidOrThrowException(createMinimalEvent()));
    }
    
    private Event createMinimalEvent() {
        Event event = new Event();
        event.setName("TV event");
        event.setEventStart(LocalDateTime.of(2019, 12, 24, 15, 0, 0));
        return event;
    }
    
    @Test
    void ensureEventWithReasonableDateTimeSpecificationsIsValid() {
        assertDoesNotThrow(this::executeValidationWithReasonableDateSettings);
    }
    
    private Event executeValidationWithReasonableDateSettings() {
        return eventValidator.ensureEventIsValidOrThrowException(addCorrectDateToMinimalEvent(createMinimalEvent()));
    }
    
    private Event addCorrectDateToMinimalEvent(Event minimalEvent) {
        LocalDateTime eventStart = minimalEvent.getEventStart();
        minimalEvent.setEventEnd(eventStart.plusHours(2));
        minimalEvent.setDeadlineRVSP(eventStart.minusDays(2).minusHours(6));
        return minimalEvent;
    }
    
    @Test
    void ensureEventWithBadDateTimeSpecificationIsNotValid() {
        assertThrows(IllegalArgumentException.class, this::executeValidationWithBadDateSettings);
    }
    
    private void executeValidationWithBadDateSettings() {
        eventValidator.ensureEventIsValidOrThrowException(addBadDateToMinimalEvent(createMinimalEvent()));
    }
    
    private Event addBadDateToMinimalEvent(Event minimalEvent) {
        LocalDateTime eventStart = minimalEvent.getEventStart();
        minimalEvent.setEventEnd(eventStart.minusDays(1));
        minimalEvent.setDeadlineRVSP(eventStart.plusHours(5));
        return minimalEvent;
    }
    
}
