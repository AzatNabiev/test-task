package ru.itis.javalab.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.itis.javalab.dto.EventDto;
import ru.itis.javalab.exception.IncorrectGivenData;
import ru.itis.javalab.form.EventForm;
import ru.itis.javalab.services.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName(("UserAddController is working when"))
public class EventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        LocalDate eventDate = LocalDate.of(29, 12, 25);
        LocalTime eventTime = LocalTime.of(1, 20, 31);
        LocalDateTime eventStarts = LocalDateTime.of(eventDate, eventTime);
        eventDate = LocalDate.of(29, 12, 25);
        eventTime = LocalTime.of(1, 30, 31);
        LocalDateTime eventEnds = LocalDateTime.of(eventDate, eventTime);

        EventForm eventForm = EventForm.builder()
                .logins(Arrays.asList("test@gmail.com", "test1@gmail.com"))
                .eventStarts("2016-12-25 19:00:13")
                .eventEnds("2016-12-25 20:30:00")
                .build();
        List<EventDto> events = new ArrayList<>();
        EventDto returnedEvent = EventDto.builder().eventStarts(eventStarts)
                .eventEnds(eventEnds)
                .build();
        events.add(returnedEvent);
        when(eventService.getFreeTime(eventForm)).thenReturn(events);

        EventForm eventForm2 = EventForm.builder()
                .login("test@gmail.com")
                .eventStarts("2016-12-25 19:00:13")
                .eventEnds("2016-12-25 20:30:00")
                .build();

        when(eventService.addEvent(eventForm2)).thenReturn(EventDto.builder()
                .login("test@gmail.com")
                .name("JavaLab")
                .eventStarts(eventStarts)
                .eventEnds(eventEnds)
                .build());

        when(eventService.addEvents(eventForm)).thenReturn(
                EventDto.builder()
                        .logins(Arrays.asList("test@gmail.com", "test1@gmail.com"))
                        .eventStarts(eventStarts)
                        .eventEnds(eventEnds)
                        .build());


        doThrow(new IncorrectGivenData("Incorrect given data"))
                .when(eventService)
                .getFreeTime(EventForm.builder()
                        .logins(Arrays.asList("testgmail.com", "test1@gmail.com")).build());

        doThrow(new IncorrectGivenData("Incorrect given data"))
                .when(eventService)
                .addEvent(EventForm.builder()
                        .login("testgmail.com").build());

        doThrow(new IncorrectGivenData("Incorrect given data"))
                .when(eventService)
                .addEvents(EventForm.builder()
                        .logins(Arrays.asList("testgmail.com", "test1@gmail.com")).build());
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("getFreeTime() is working")
    class FreeTime {
        @Test
        public void show_free_time() throws Exception {
            mockMvc.perform(post("/events/time/free")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "    \"logins\":[\"test1@gmail.com\",\"test@gmail.com\"]\n" +
                            "}")
                    .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        @Test
        public void throws_exception_for_show_free_time() throws Exception {
            mockMvc.perform(post("/events/time/free")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "   \"logins\":[\"tesgmail.com\",\"test@gmail.com\"]\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addEvent() is working")
    class EventAdding {

        @Test
        public void add_event_test() throws Exception {
            String jsonVal = "{\n" +
                    "\"login\":\"test@gmail.com\",\n" +
                    "\"logins\":null,\n" +
                    "\"name\":\"JavaLab\",\n" +
                    "\"eventStarts\":\"2029-12-25 01:20:31\",\n" +
                    "\"eventEnds\":\"2029-12-25 01:30:31\"\n" +
                    "}";
            mockMvc.perform(post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonVal)
                    .characterEncoding("utf-8"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        }

        @Test
        public void throws_exception_for_event_adding() throws Exception {
            mockMvc.perform(post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"login\": \"testgmail.com\"\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addEvents() is working")
    class EventsAdding {
        @Test
        public void add_event_for_users() throws Exception {
            mockMvc.perform(post("/events/several")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "   \"logins\":[\"test1@gmail.com\",\"test@gmail.com\"],\n" +
                            "   \"name\": \"Созвон по проекту\",\n" +
                            "   \"eventStarts\": \"2019-12-25 19:00:13\",\n" +
                            "   \"eventEnds\": \"2019-12-25 20:30:00\"\n" +
                            "}")
                    .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        @Test
        public void throws_exception_for_several_user_event_adding() throws Exception {
            mockMvc.perform(post("/events/several")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "   \"logins\":[\"tesgmail.com\",\"test@gmail.com\"]\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }
    }
}
