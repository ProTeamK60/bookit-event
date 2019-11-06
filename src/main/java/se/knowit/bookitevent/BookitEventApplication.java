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

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
			ZonedDateTime st = ZonedDateTime.of(2019, 11, 13, 17, 56, 0,0, ZoneId.systemDefault());
			Event event = new Event();
			event.setName("DEFAULT_EVENT");
			event.setId(1L);
			event.setEventId(UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b"));
			event.setEventStart(st);
			event.setEventEnd(st.plusHours(2));
			event.setDeadlineRVSP(st.minusDays(2));
			event.setLocation("K60");
			event.setOrganizer("Erik");
			event.setDescription("Ett himla bra event!");
			eventService.save(event);
			
			event = new Event();
			event.setName("DEFAULT_EVENT");
			event.setId(2L);
			event.setEventId(UUID.fromString("82ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b"));
			event.setEventStart(st.plusMonths(6));
			event.setEventEnd(st.plusMonths(6).plusHours(2));
			event.setDeadlineRVSP(st.plusMonths(6).minusDays(2));
			event.setLocation("K60");
			event.setOrganizer("Erik");
			event.setDescription("Ännu ett himla bra event!");
			eventService.save(event);
			
		};
			
	}

}
