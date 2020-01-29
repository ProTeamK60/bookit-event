package se.knowit.bookitevent.model;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
public class Event {

	private Long id;
	private UUID eventId;
	private String name;
	private String description;
	private Instant eventStart;
	private Instant eventEnd;
	private Instant deadlineRVSP;
	private String location;
	private String organizer;
	private List<Option> options;

	public boolean haveEventId(UUID other) {
		return Objects.equals(eventId, other);
	}
}
