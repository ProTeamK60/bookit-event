package se.knowit.bookitevent.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.TimeSupport;
import se.knowit.bookitevent.dto.TimeSupportImpl;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.repository.EventRepository;
import se.knowit.bookitevent.repository.map.EventRepositoryMapImpl;

@Configuration
public class BookitSpringConfiguration {

    @Bean
    @Primary
    @Autowired
    public EventRepository mapBasedRepository(KafkaProducerService<String, EventDTO> kafkaProducerService) {
        return new EventRepositoryMapImpl(kafkaProducerService);
    }

    @Bean
    public TimeSupport timeSupport() {
        return new TimeSupportImpl();
    }
}
