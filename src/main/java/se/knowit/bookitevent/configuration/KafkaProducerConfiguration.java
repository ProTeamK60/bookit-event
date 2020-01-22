package se.knowit.bookitevent.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.kafka.producer.KafkaEventProducerServiceImpl;
import se.knowit.bookitevent.servicediscovery.DiscoveryService;
import se.knowit.bookitevent.servicediscovery.DiscoveryServiceResult;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Autowired
    private DiscoveryService discoveryService;

    @Bean
    public ProducerFactory<String, EventDTO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        DiscoveryServiceResult result = discoveryService.discoverInstances("bookit", "kafka");
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, result.getAddresses());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory(configProps);
    }

    @Bean
    public KafkaTemplate<String, EventDTO> EventTemplate() {
        return new KafkaTemplate(producerFactory());
    }

    @Bean
    @Autowired
    public KafkaProducerService kafkaServiceImpl(KafkaTemplate<String, EventDTO> eventTemplate) {
        return new KafkaEventProducerServiceImpl(eventTemplate);
    }

}

