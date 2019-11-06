package se.knowit.bookitevent.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private UUID eventId;
	private String name;
	private String description;
	private ZonedDateTime eventStart;
	private ZonedDateTime eventEnd;
	private ZonedDateTime deadlineRVSP;
	private String location;
	private String organizer;

	public boolean haveEventId(UUID other) {
		return Objects.equals(eventId, other);
	}
}
