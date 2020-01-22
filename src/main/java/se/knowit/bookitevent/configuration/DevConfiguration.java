package se.knowit.bookitevent.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import se.knowit.bookitevent.servicediscovery.DiscoveryService;
import se.knowit.bookitevent.servicediscovery.LocalDiscoveryServiceImpl;

@Profile("dev")
@Configuration
public class DevConfiguration {

    @Bean
    @Autowired
    public DiscoveryService localDiscoveryService(Environment environment) { return new LocalDiscoveryServiceImpl(environment); }
}
