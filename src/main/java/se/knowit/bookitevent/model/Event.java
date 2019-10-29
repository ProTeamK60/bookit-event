package se.knowit.bookitevent.model;

import java.time.LocalDateTime;
import java.util.UUID;

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
	private UUID eventId;
	private String name;
	private String description;
	private LocalDateTime eventStart;
	private LocalDateTime eventEnd;
	private LocalDateTime deadlineRVSP;
	private String location;
	private String organizer;

}
