package se.knowit.bookitevent;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
	

	@Test
	public void testCreateNewEvent() throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://" + hostname + ":" + port + "/api/v1/events";
	
		String jsonEvent = "{\"eventId\":\"1a0b7c8b-c0d5-4ab2-8c63-5cf1ad0b439b\",\n" + 
				"                \"name\":\"IT event\",\n" + 
				"                \"description\":\"IT description\" , \n" + 
				"                \"eventStart\":\"2019-12-02T18:00:00\", \n" + 
				"                \"eventEnd\":\"2019-12-02T22:00:00\", \n" + 
				"                \"deadlineRVSP\":\"2019-11-02T17:00:00\", \n" + 
				"                \"location\":\"K60\",\n" + 
				"                \"organizer\":\"AliAfs\"}";
		
		HttpEntity<String> entity = new HttpEntity<String>(jsonEvent, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		HttpHeaders response = responseEntity.getHeaders();
		URI uri = response.getLocation();
		assertTrue(uri.toString().indexOf("1a0b7c8b-c0d5-4ab2-8c63-5cf1ad0b439b") != -1);
	}

}
