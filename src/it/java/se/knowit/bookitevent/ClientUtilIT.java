package se.knowit.bookitevent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import se.knowit.bookitevent.model.Event;

public class ClientUtilIT {

	
	String hostname = "localhost", port = "8080";
	
	@Test
	public void testGetEventById() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://"+hostname+":"+ port +"/api/v1/events/{id}";

		Map<String, String> par = new HashMap<>();
		par.put("id", "72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
		Event eve = restTemplate.getForObject(url, Event.class, par);
		System.out.println("Id:" + eve.getId() + " : " + eve.getName() + " : " + eve.getDescription());
		Assertions.assertTrue(eve.getId() == 1L);
		Assertions.assertTrue(eve.getDescription().contentEquals("Ett himla bra event!"));
	}

	@Test
	public void testGetAllEvents() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://"+hostname+":"+ port +"/api/v1/events";

		Event allEvents[] = restTemplate.getForObject(url, Event[].class);
		Assertions.assertEquals(allEvents.length, 2);

		Assertions.assertTrue(Arrays.stream(allEvents).map(e -> e.getId()).anyMatch(id -> id.equals(1L)));

		Assertions.assertTrue(Arrays.stream(allEvents).map(e -> e.getId()).anyMatch(id -> id.equals(2L)));

		Assertions.assertTrue(Arrays.stream(allEvents).map(e -> e.getDescription())
				.anyMatch(des -> des.equals("Ett himla bra event!")));
		Assertions.assertTrue(Arrays.stream(allEvents).map(e -> e.getDescription())
				.anyMatch(des -> des.equals("Ännu ett himla bra event!")));
		Arrays.stream(allEvents).forEach(
				eve -> System.out.println("Id:" + eve.getId() + " : " + eve.getName() + " : " + eve.getDescription()));
	}

}