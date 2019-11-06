package se.knowit.bookitevent;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.time.LocalDateTime;
import java.util.UUID;


@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
public class BookitEventApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BookitEventApplication.class, args);
    }
    
    
	@Bean
	CommandLineRunner init(EventService eventService) {
		return args -> {
			LocalDateTime st = LocalDateTime.of(2019, 12, 6, 18, 00);
			Event event = new Event();
			event.setName("Julfest");
			event.setId(1L);
			event.setEventId(UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b"));
			event.setEventStart(st);
			event.setEventEnd(st.plusHours(4));
			event.setDeadlineRVSP(st.minusDays(27));
			event.setLocation("Norrmalm");
			event.setOrganizer("Knowit");
			event.setDescription("Julbord!");
			eventService.save(event);
			
			event = new Event();
			event.setName("Sierra Nevada");
			event.setId(2L);
			st = LocalDateTime.of(2020, 3, 20, 8, 30);
			event.setEventId(UUID.fromString("82ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b"));
			event.setEventStart(st);
			event.setEventEnd(st.plusDays(3));
			event.setDeadlineRVSP(st.minusMonths(2));
			event.setLocation("Spain");
			event.setOrganizer("Knowit");
			event.setDescription("Skidkonferans!");
			eventService.save(event);
			
		};
			
	}

}
