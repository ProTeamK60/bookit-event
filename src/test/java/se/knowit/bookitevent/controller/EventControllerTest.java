package se.knowit.bookitevent.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.knowit.bookitevent.model.Event;
import se.knowit.bookitevent.service.EventService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
