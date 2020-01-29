package se.knowit.bookitevent;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.OptionDTO;
import se.knowit.bookitevent.kafka.producer.KafkaProducerService;
import se.knowit.bookitevent.service.CreateOrUpdateCommand;
import se.knowit.bookitevent.service.EventService;


@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		KafkaAutoConfiguration.class
})
public class BookitEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookitEventApplication.class, args);
    }

	@Bean
	CommandLineRunner init(EventService eventService, KafkaProducerService<String, EventDTO> kafkaService) {
		return args -> {
			ZonedDateTime st = ZonedDateTime.of(2019, 12, 6, 18, 0, 0, 0, ZoneId.systemDefault());
			EventDTO event = new EventDTO();
			event.setName("Julfest");
			event.setEventId("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
			event.setEventStart(st.toEpochSecond());
			event.setEventEnd(st.plusHours(4).toEpochSecond());
			event.setDeadlineRVSP(st.minusDays(27).toEpochSecond());
			event.setLocation("Norrmalm");
			event.setOrganizer("Knowit");
			event.setDescription("Julbord!");

			CreateOrUpdateCommand createOrUpdateCommand = new CreateOrUpdateCommand(eventService, kafkaService);
			createOrUpdateCommand.apply(event);
			
			event = new EventDTO();
			
			List<OptionDTO> options = new ArrayList<OptionDTO>(); 
			options.add(new OptionDTO(1,"oneOption","Vad vill du äta?", "fisk,fågel,kött,vegetariskt"));
			options.add(new OptionDTO(2, "multiOption", "vilka aktiviteter vill du delta i?", "simmning,bio,skridskor,skidor"));
			options.add(new OptionDTO(3, "freeText", "annat viktigt info(allergi etc)", ""));
			event.setOptions(options);
			createOrUpdateCommand.apply(event);
			
			
			event = new EventDTO();

			event.setName("Sierra Nevada");
			st = ZonedDateTime.of(2020, 3, 20, 8, 30, 0, 0, ZoneId.systemDefault());
			event.setEventId("82ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
			event.setEventStart(st.toEpochSecond());
			event.setEventEnd(st.plusDays(3).toEpochSecond());
			event.setDeadlineRVSP(st.minusMonths(2).toEpochSecond());
			event.setLocation("Spain");
			event.setOrganizer("Knowit");
			event.setDescription("Skidkonferans!");

			createOrUpdateCommand = new CreateOrUpdateCommand(eventService, kafkaService);
			createOrUpdateCommand.apply(event);

			event.setOptions(options);
			createOrUpdateCommand.apply(event);
			
		};

	}

}
