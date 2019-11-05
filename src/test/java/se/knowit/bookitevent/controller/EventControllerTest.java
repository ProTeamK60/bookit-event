package se.knowit.bookitevent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.knowit.bookitevent.dto.EventDTO;
import se.knowit.bookitevent.dto.EventMapper;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {
    private static final Long DEFAULT_ID = 1L;
    private static final UUID DEFAULT_UUID = UUID.fromString("72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b");
    private static final Event DEFAULT_EVENT = buildDefaultEvent();
    
    private static Event buildDefaultEvent() {
        LocalDateTime startTime = LocalDateTime.of(2019, 11, 13, 17, 56);
        Event event = new Event();
        event.setName("DEFAULT_EVENT");
        event.setId(DEFAULT_ID);
        event.setEventId(DEFAULT_UUID);
        event.setEventStart(startTime);
        event.setEventEnd(startTime.plusHours(2));
        event.setDeadlineRVSP(startTime.minusDays(2));
        event.setLocation("K60");
        event.setOrganizer("Erik");
        event.setDescription("Ett himla bra event!");
        return event;
    }
    
    @Mock
    private EventService eventService;
    
    @InjectMocks
    private EventController eventController;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }
    
    @Test
    void getRequestFor_AllEvents_ShouldReturnAllEvents() throws Exception {
        when(eventService.findAll()).thenReturn(Set.of(DEFAULT_EVENT));
        
        mockMvc.perform(get("/api/v1/events/")).andExpect(status().isOk());
    }
    
    @Test
    void getRequestFor_AllEvents_ShouldReturnErrorCode_404_WhenNoEventsExists() throws Exception {
        when(eventService.findAll()).thenReturn(Collections.emptySet());
    
        mockMvc.perform(get("/api/v1/events/"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getRequestFor_ExistingEventId_ShouldReturnCorrectEvent() throws Exception {
        when(eventService.findByEventId(any())).thenReturn(Optional.of(DEFAULT_EVENT));
        
        mockMvc.perform(get("/api/v1/events/" + DEFAULT_UUID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventId").value(DEFAULT_UUID.toString()));
    }
    
    @Test
    void getRequestFor_NonExistingEventId_ShouldReturnErrorCode_404() throws Exception {
        when(eventService.findByEventId(any())).thenReturn(Optional.empty());
    
        mockMvc.perform(get("/api/v1/events/ea4ab6c0-8a73-4e9b-b28a-7bb9e0f87b18"))
                .andExpect(status().isNotFound());
    
    }
    
    @Test
    void postRequest_WithValidDataAndNoEventId_ShouldReturn_LocationOfTheSavedEvent() throws Exception {
        String incomingJson = "{\"eventId\":\"\"," +
                "\"name\":\"A test event\"," +
                "\"description\":\"Test description\"," +
                "\"eventStart\":\"1970-01-02T01:00:00\"," +
                "\"eventEnd\":\"1970-01-02T01:01:00\"," +
                "\"deadlineRVSP\":\"1970-01-01T18:00:00\"," +
                "\"location\":\"K60\"," +
                "\"organizer\":\"Ola\"}";
    
        EventDTO eventDTO = getEventDTOFromJson(incomingJson);
        EventMapper eventMapper = new EventMapper();
        Event savedEvent = eventMapper.fromDTO(eventDTO);
        savedEvent.setEventId(DEFAULT_UUID);
        EventDTO savedEventDTO = eventMapper.toDTO(savedEvent);
        
        when(eventService.save(eq(eventMapper.fromDTO(eventDTO)))).thenReturn(savedEvent);
        
        MvcResult result = mockMvc.perform(
                post("/api/v1/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomingJson)
        )
        .andExpect(status().isCreated())
        .andExpect(header().string("location", matchesPattern("/api/v1/events/[-a-z0-9]+")))
        .andReturn();
    
        assertReturnedLocationProvidesCorrectJson(savedEvent, savedEventDTO, result);
    }
    
    @Test
    void postRequestWithValidDataAndExistingEventIdShouldReturnLocationOfTheUpdatedEvent() throws Exception {
        String incomingJson = "{\"eventId\":\""+
                DEFAULT_UUID.toString() +
                "\"," +
                "\"name\":\"A test event\"," +
                "\"description\":\"Test description\"," +
                "\"eventStart\":\"1970-01-02T01:00:00\"," +
                "\"eventEnd\":\"1970-01-02T01:01:00\"," +
                "\"deadlineRVSP\":\"1970-01-01T18:00:00\"," +
                "\"location\":\"K60\"," +
                "\"organizer\":\"Ola\"}";
    
        EventDTO eventDTO = getEventDTOFromJson(incomingJson);
        EventMapper eventMapper = new EventMapper();
        Event savedEvent = eventMapper.fromDTO(eventDTO);
        EventDTO savedEventDTO = eventMapper.toDTO(savedEvent);
    
        when(eventService.save(eq(eventMapper.fromDTO(eventDTO)))).thenReturn(savedEvent);
    
        MvcResult result = mockMvc.perform(
                post("/api/v1/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incomingJson)
        )
                .andExpect(status().isAccepted())
                .andExpect(header().string("location", matchesPattern("/api/v1/events/[-a-z0-9]+")))
                .andReturn();
    
        assertReturnedLocationProvidesCorrectJson(savedEvent, savedEventDTO, result);
    }
    
    private void assertReturnedLocationProvidesCorrectJson(Event savedEvent, EventDTO savedEventDTO, MvcResult result) throws Exception {
        String location = result.getResponse().getHeader("location");
        when(eventService.findByEventId(eq(DEFAULT_UUID))).thenReturn(Optional.of(savedEvent));
        MvcResult getResult = mockMvc.perform(get(location).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(savedEventDTO, getEventDTOFromJson(getResult.getResponse().getContentAsString()));
    }
    
    private EventDTO getEventDTOFromJson(String incomingJson) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.readValue(incomingJson, EventDTO.class);
    }
    
    @Test
    void postRequestWithInvalidEventDataShouldReturnA_HTTP_400_Response() throws Exception {
        when(eventService.save(any())).thenThrow(IllegalArgumentException.class);
    
        mockMvc.perform(
                post("/api/v1/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fake\":\"true\"}")
        ).andExpect(status().isBadRequest());
    }
}
