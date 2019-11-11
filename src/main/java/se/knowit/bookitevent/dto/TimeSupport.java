package se.knowit.bookitevent.dto;

import java.time.Instant;

public interface TimeSupport {
    Instant getInstantFromEpochMilli(Long epochMilli);
    Long getEpochMilliFromInstant(Instant instant);
}
