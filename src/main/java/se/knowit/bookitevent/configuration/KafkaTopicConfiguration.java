package se.knowit.bookitevent.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import se.knowit.bookitevent.servicediscovery.DiscoveryService;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfiguration {

    private DiscoveryService discoveryService;

    public KafkaTopicConfiguration(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String,Object> config = new HashMap<>();
        String kafkaAddress = discoveryService.discoverInstance("kafka");
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        return new KafkaAdmin(config);
    }

}
