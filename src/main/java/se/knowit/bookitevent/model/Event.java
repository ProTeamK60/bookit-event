package se.knowit.bookitevent.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String eventId;

	private String name;
	private String description;
	private String eventStart;
	private String eventDate;
	private String deadlineRVSP;
	private String location;
	private String organizer;

}
