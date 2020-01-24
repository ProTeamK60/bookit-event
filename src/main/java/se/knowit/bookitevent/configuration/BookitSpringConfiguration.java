package se.knowit.bookitevent.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.knowit.bookitevent.dto.TimeSupport;
import se.knowit.bookitevent.dto.TimeSupportImpl;
import se.knowit.bookitevent.repository.EventRepository;
import se.knowit.bookitevent.repository.EventRepositoryMapImpl;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    public EventRepository mapBasedServiceImplementation() {
        return new EventRepositoryMapImpl();
    }

    @Bean
    public TimeSupport timeSupport() {
        return new TimeSupportImpl();
    }
}
