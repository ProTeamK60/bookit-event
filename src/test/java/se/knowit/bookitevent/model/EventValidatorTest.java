package se.knowit.bookitevent.model;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
        event.setEventStart(ZonedDateTime.of(2019, 12, 24, 15, 0, 0,0, ZoneId.systemDefault()));
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
        ZonedDateTime eventStart = minimalEvent.getEventStart();
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
        ZonedDateTime eventStart = minimalEvent.getEventStart();
        minimalEvent.setEventEnd(eventStart.minusDays(1));
        minimalEvent.setDeadlineRVSP(eventStart.plusHours(5));
        return minimalEvent;
    }
    
    @Test
    void ensureEventWithIncorrectRsvpDateIsInvalid() {
        assertThrows(IllegalArgumentException.class, this::executeValidationWithBadRsvpSettings);
    }
    
    private void executeValidationWithBadRsvpSettings() {
        eventValidator.ensureEventIsValidOrThrowException(addBadRsvpToMinimalEvent(createMinimalEvent()));
    }
    
    private Event addBadRsvpToMinimalEvent(Event minimalEvent) {
        ZonedDateTime eventStart = minimalEvent.getEventStart();
        minimalEvent.setDeadlineRVSP(eventStart.plusMinutes(10));
        return minimalEvent;
    }
}
