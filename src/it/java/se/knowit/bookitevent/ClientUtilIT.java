package se.knowit.bookitevent;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import se.knowit.bookitevent.dto.EventDTO;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ClientUtilIT {
 
	String hostname = "localhost", port = "8080";
	
	@Test
	public void testGetEventById() {
		System.out.println("ClientUtilIT.testGetEventById");
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
		System.out.println("ClientUtilIT.testGetAllEvents");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + hostname + ":" + port + "/api/v1/events";

		EventDTO[] allEvents = restTemplate.getForObject(url, EventDTO[].class);
		assertTrue(allEvents != null);
	}
	

	@Test
	public void testCreateNewEvent() {
		System.out.println("ClientUtilIT.testCreateNewEvent");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + hostname + ":" + port + "/api/v1/events";
	
		String jsonEvent = "{\"eventId\":\"1a0b7c8b-c0d5-4ab2-8c63-5cf1ad0b439b\",\n" + 
				"                \"name\":\"IT event\",\n" + 
				"                \"description\":\"IT description\" , \n" + 
				"                \"eventStart\":\"90000000\", \n" + 
				"                \"eventEnd\":\"90060000\", \n" + 
				"                \"deadlineRVSP\":\"64800000\", \n" + 
				"                \"location\":\"K60\",\n" + 
				"                \"organizer\":\"AliAfs\"}";
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonEvent, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		HttpHeaders response = responseEntity.getHeaders();
		URI uri = response.getLocation();
		assertTrue(uri.toString().indexOf("1a0b7c8b-c0d5-4ab2-8c63-5cf1ad0b439b") != -1);
	}

}
