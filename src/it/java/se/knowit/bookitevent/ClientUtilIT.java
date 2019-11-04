package se.knowit.bookitevent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import se.knowit.bookitevent.model.Event;

public class ClientUtilIT {
	
	@Test
	public void getEventByIdDemo() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/api/v1/events/{id}";
		
		Map<String, String> par = new HashMap<>();
        par.put("id", "72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");

		Event eve = restTemplate.getForObject(url, Event.class, par);
		System.out.println("Id:" + eve.getId() + " : " + eve.getName() + " : " + eve.getDescription());
		Assert.hasText(Long.toString(eve.getId()),"1");
		Assert.hasText(eve.getDescription(), "Ett himla bra event!");
	}

	@Test
	public void getAllEventsDemo() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/api/v1/events";
		
        Event allEvents[] = restTemplate.getForObject(url, Event[].class);
        
        Assert.hasText(Long.toString(allEvents[0].getId()), "1");
		Assert.hasText(allEvents[0].getDescription(), "Ett himla bra event!");
		
        Assert.hasText(Long.toString(allEvents[1].getId()),"2");
		Assert.hasText(allEvents[1].getDescription(), "Ännu ett himla bra event!");
		
		Arrays.stream(allEvents).forEach(eve -> 
		System.out.println("Id:" + eve.getId() + " : " + eve.getName() + " : " + eve.getDescription()));

	}
	
	/*
	public static void main(String args[]) {
		ClientUtil util = new ClientUtil();
		util.getAllEventsDemo();
		util.getEventByIdDemo();
	}*/
}