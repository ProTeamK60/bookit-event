package se.knowit.bookitevent;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;


@SpringBootApplication
public class BookitEventApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BookitEventApplication.class, args);
    }
    
    
	@Bean
	CommandLineRunner init(EventService eventService) {
		return args -> {
			LocalDateTime st = LocalDateTime.of(2019, 11, 13, 17, 56);
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
			    
			};
			
	}

}
