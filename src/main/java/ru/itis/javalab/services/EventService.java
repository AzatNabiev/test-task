package ru.itis.javalab.services;

import ru.itis.javalab.dto.EventDto;
import ru.itis.javalab.form.EventForm;
import ru.itis.javalab.models.Event;
import ru.itis.javalab.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {
    List<EventDto> getFreeTime(EventForm eventForm);

    List<User> checkExistingUsers(List<String> userLogins);

    Optional<Event> findIntersection(List<Event> mergedFreeTime, Event event);

    List<Event> getMergedCommonSchedule(List<Event> mergedFreeTime, List<Event> usersFreeTime);

    EventDto addEvent(EventForm eventForm);

    Boolean isEventExist(LocalDateTime eventStarts, LocalDateTime eventEnds, User user);

    EventDto addEvents(EventForm eventForm);
}
