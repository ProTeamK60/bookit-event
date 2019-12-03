package se.knowit.bookitevent.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.*;
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
        event.setEventStart(ZonedDateTime.of(2019, 12, 24, 15, 0, 0, 0, ZoneId.systemDefault()).toInstant());
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
        Instant eventStart = minimalEvent.getEventStart();
        minimalEvent.setEventEnd(plusDays(eventStart, 2));
        minimalEvent.setDeadlineRVSP(minusDays(minusHours(eventStart, 6), 3));
        return minimalEvent;
    }
    
    private Instant minusHours(Instant base, Integer amount) {
        return base.minus(amount, HOURS);
    }
    
    private Instant minusDays(Instant eventStart, Integer amount) {
        return eventStart.minus(amount, DAYS);
    }
    
    private Instant plusDays(Instant base, Integer amount) {
        return base.plus(amount, DAYS);
    }
    
    private Instant plusHours(Instant base, Integer amount) {
        return base.plus(amount, HOURS);
    }
    
    private Instant plusMinutes(Instant base, Integer amount) {
        return base.plus(amount, MINUTES);
    }
    
    @Test
    void ensureEventWithBadDateTimeSpecificationIsNotValid() {
        assertThrows(IllegalArgumentException.class, this::executeValidationWithBadEventEndSettings);
        assertThrows(IllegalArgumentException.class, this::executeValidationWithBadDeadlineRSVPSettings);
    }
    
    private void executeValidationWithBadEventEndSettings() {
        eventValidator.ensureEventIsValidOrThrowException(addBadEventEndToMinimalEvent(createMinimalEvent()));
    }
    
    private Event addBadEventEndToMinimalEvent(Event minimalEvent) {
        Instant eventStart = minimalEvent.getEventStart();
        minimalEvent.setEventEnd(minusDays(eventStart, 1));
        return minimalEvent;
    }
    
    private void executeValidationWithBadDeadlineRSVPSettings() {
        eventValidator.ensureEventIsValidOrThrowException(addBadDeadlineRSVPToMinimalEvent(createMinimalEvent()));
    }
    
    private Event addBadDeadlineRSVPToMinimalEvent(Event minimalEvent) {
        Instant eventStart = minimalEvent.getEventStart();
        minimalEvent.setDeadlineRVSP(plusHours(eventStart, 5));
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
        Instant eventStart = minimalEvent.getEventStart();
        minimalEvent.setDeadlineRVSP(plusMinutes(eventStart, 10));
        return minimalEvent;
    }
    
    @Test
    void ensureEventWithoutNameIsInvalid() {
        assertThrows(IllegalArgumentException.class, this::executeValidationWithNullNameEvent);
        assertThrows(IllegalArgumentException.class, this::executeValidationWithBlankNameEvent);
        assertThrows(IllegalArgumentException.class, this::executeValidationWithEmptyNameEvent);
    }
    
    private void executeValidationWithEmptyNameEvent() {
        Event minimalEvent = createMinimalEvent();
        minimalEvent.setName("");
        eventValidator.ensureEventIsValidOrThrowException(minimalEvent);
    }
    
    private void executeValidationWithBlankNameEvent() {
        Event minimalEvent = createMinimalEvent();
        minimalEvent.setName("\t  ");
        eventValidator.ensureEventIsValidOrThrowException(minimalEvent);
    }
    
    private void executeValidationWithNullNameEvent() {
        Event minimalEvent = createMinimalEvent();
        minimalEvent.setName(null);
        eventValidator.ensureEventIsValidOrThrowException(minimalEvent);
    }
    
    @Test
    void ensureEventWithoutStartDateIsInvalid() {
        assertThrows(IllegalArgumentException.class, this::executeValidationWithNullStartDateEvent);
    }
    
    private void executeValidationWithNullStartDateEvent() {
        Event minimalEvent = createMinimalEvent();
        minimalEvent.setEventStart(null);
        eventValidator.ensureEventIsValidOrThrowException(minimalEvent);
    }
}
