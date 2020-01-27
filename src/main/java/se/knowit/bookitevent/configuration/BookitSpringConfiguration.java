package se.knowit.bookitevent.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import se.knowit.bookitevent.dto.TimeSupport;
import se.knowit.bookitevent.dto.TimeSupportImpl;
import se.knowit.bookitevent.repository.EventRepository;
import se.knowit.bookitevent.repository.map.EventRepositoryMapImpl;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    @Primary
    public EventRepository mapBasedRepository() {
        return new EventRepositoryMapImpl();
    }

    @Bean
    public TimeSupport timeSupport() {
        return new TimeSupportImpl();
    }
}
