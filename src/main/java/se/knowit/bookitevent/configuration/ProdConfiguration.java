package se.knowit.bookitevent.configuration;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.knowit.bookitevent.servicediscovery.AwsDiscoveryServiceImpl;
import se.knowit.bookitevent.servicediscovery.DiscoveryService;

@Profile("prod")
@Configuration
public class ProdConfiguration {

    @Value("${aws.discovery.region}")
    private String awsRegion;

    @Bean
    public DiscoveryService awsDiscoveryServiceImpl() {
        return new AwsDiscoveryServiceImpl(awsServiceDiscoveryClient());
    }

    @Bean
    public AWSServiceDiscovery awsServiceDiscoveryClient() {
        return AWSServiceDiscoveryClientBuilder
                .standard()
                .withRegion(awsRegion)
                .build();
    }
}
