package se.knowit.bookitevent.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import se.knowit.bookitevent.servicediscovery.DiscoveryService;
import se.knowit.bookitevent.servicediscovery.LocalDiscoveryServiceImpl;

@Profile("dev")
@Configuration
public class DevConfiguration {

    @Profile("dev")
    @Bean
    public DiscoveryService localServiceDiscoveryImpl(Environment env) { return new LocalDiscoveryServiceImpl(env);}

}
