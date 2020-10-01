package se.knowit.bookitevent.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TimeSupportImplTest {

    private TimeSupportImpl support = new TimeSupportImpl();

    @Test
    void getNullOutputForNullInputWhenConvertingToInstant() {
        assertNull(support.getInstantFromEpochMilli(null));
    }
    
    @Test
    void getInstantWhenProvidingNonNegativeLongValue() {
        assertNotNull(support.getInstantFromEpochMilli(1L));
    }
    
    @Test
    void getNullOutputForNullInputWhenConvertingToLong() {
        assertNull(support.getEpochMilliFromInstant(null));
    }
    
    @Test
    void getLongValueWhenProvidingNonNullInstant() {
        assertNotNull(support.getEpochMilliFromInstant(Instant.now()));
    }
}
