package se.knowit.bookitevent.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitevent.dto.TimeSupport;
import se.knowit.bookitevent.dto.TimeSupportImpl;
import se.knowit.bookitevent.service.EventService;
import se.knowit.bookitevent.service.map.EventServiceMapImpl;
import se.knowit.bookitevent.servicediscovery.AwsDiscoveryServiceImpl;
import se.knowit.bookitevent.servicediscovery.DiscoveryService;

@Configuration
public class BookitSpringConfiguration {
    
    @Profile("dev")
    @Bean
    public EventService mapBasedServiceImplementation() {
        return new EventServiceMapImpl();
    }

    @Profile("prod")
    @Bean
    public DiscoveryService prodServiceDiscoveryImpl() { return new AwsDiscoveryServiceImpl();}

    @Bean
    public TimeSupport timeSupport() {
        return new TimeSupportImpl();
    }
}
