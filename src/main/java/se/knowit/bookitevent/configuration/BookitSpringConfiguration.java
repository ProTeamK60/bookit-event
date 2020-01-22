package se.knowit.bookitevent.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.knowit.bookitevent.dto.TimeSupport;
import se.knowit.bookitevent.dto.TimeSupportImpl;
import se.knowit.bookitevent.service.EventService;
import se.knowit.bookitevent.service.map.EventServiceMapImpl;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    public EventService mapBasedServiceImplementation() {
        return new EventServiceMapImpl();
    }

    @Bean
    public TimeSupport timeSupport() {
        return new TimeSupportImpl();
    }
}
