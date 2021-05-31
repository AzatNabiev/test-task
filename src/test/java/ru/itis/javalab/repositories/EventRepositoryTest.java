package ru.itis.javalab.repositories;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.itis.javalab.models.Event;
import ru.itis.javalab.models.User;

import static junit.framework.TestCase.assertEquals;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES, beanName = "dataSource", provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    public void check_existing_event(){
        LocalDate eventDate = LocalDate.of(2019,12,25);
        LocalTime eventTime = LocalTime.of(19,0,13);
        LocalDateTime eventStarts = LocalDateTime.of(eventDate,eventTime);

        eventDate = LocalDate.of(2019,12,25);
        eventTime = LocalTime.of(20,30,13);

        LocalDateTime eventEnds = LocalDateTime.of(eventDate,eventTime);
        Event testEvent = Event.builder()
                .eventStarts(eventStarts)
                .eventEnds(eventEnds)
                .user(User.builder().id(1L).build())
                .build();
        Boolean expectedBoolean = true;
        Boolean actualBoolean = eventRepository.checkExistingEvent(testEvent.getEventStarts(),testEvent.getEventEnds(),testEvent.getUser());
        assertEquals(expectedBoolean,actualBoolean);
    }

    @Test
    public void check_non_existing_event(){
        LocalDate eventDate = LocalDate.of(2019,12,25);
        LocalTime eventTime = LocalTime.of(19,0,13);
        LocalDateTime eventStarts = LocalDateTime.of(eventDate,eventTime);

        eventDate = LocalDate.of(2019,12,25);
        eventTime = LocalTime.of(20,30,13);

        LocalDateTime eventEnds = LocalDateTime.of(eventDate,eventTime);
        Event testEvent = Event.builder()
                .eventStarts(eventStarts)
                .eventEnds(eventEnds)
                .user(User.builder().id(5L).build())
                .build();
        Boolean expectedBoolean = false;
        Boolean actualBoolean = eventRepository.checkExistingEvent(testEvent.getEventStarts(),testEvent.getEventEnds(),testEvent.getUser());
        assertEquals(expectedBoolean,actualBoolean);
    }
    @Test
    void return_free_space(){
        long expectedEventsCount =2;
        List<Event> events= eventRepository.getFreeTime(1L);
        assertEquals(expectedEventsCount,events.size());
    }
    @Test
    void return_busy_period(){
        long expectedEventsCount=0;
        List<Event> events= eventRepository.getFreeTime(2L);
        assertEquals(expectedEventsCount,events.size());

    }
}
