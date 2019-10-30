package se.knowit.bookitevent.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitevent.service.EventService;
import se.knowit.bookitevent.service.map.EventServiceMapImpl;

@Configuration
public class BookitSpringConfiguration {
    
    @Profile("map")
    @Bean
    public EventService mapBasedServiceImplementation() {
        return new EventServiceMapImpl();
    }
}
