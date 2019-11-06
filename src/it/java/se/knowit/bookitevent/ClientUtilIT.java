package se.knowit.bookitevent;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import se.knowit.bookitevent.dto.EventDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientUtilIT {
 
	String hostname = "localhost", port = "8080";
	
	@Test
	public void testGetEventById() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + hostname + ":" + port + "/api/v1/events/{id}";

		Map<String, String> par = new HashMap<>();
		par.put("id", "72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
		EventDTO eve = restTemplate.getForObject(url, EventDTO.class, par);
		System.out.println("Id:" + eve.getEventId() + " : " + eve.getName() + " : " + eve.getDescription());
		assertEquals("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b", eve.getEventId());
		assertTrue(eve.getDescription().contentEquals("Julbord!"));
	}

	@Test
	public void testGetAllEvents() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + hostname + ":" + port + "/api/v1/events";

		EventDTO[] allEvents = restTemplate.getForObject(url, EventDTO[].class);
		assertEquals(allEvents.length, 2);
		
		assertTrue(Arrays.stream(allEvents).map(EventDTO::getEventId).anyMatch(id -> id.equals("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b")));

		assertTrue(Arrays.stream(allEvents).map(EventDTO::getEventId).anyMatch(id -> id.equals("82ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b")));

		assertTrue(Arrays.stream(allEvents).map(e -> e.getDescription())
				.anyMatch(des -> des.equals("Julbord!")));
		assertTrue(Arrays.stream(allEvents).map(e -> e.getDescription())
				.anyMatch(des -> des.equals("Skidkonferans!")));
		Arrays.stream(allEvents).forEach(
				eve -> System.out.println("Id:" + eve.getEventId() + " : " + eve.getName() + " : " + eve.getDescription()));
	}

}
