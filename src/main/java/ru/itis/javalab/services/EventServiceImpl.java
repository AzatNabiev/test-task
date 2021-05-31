package ru.itis.javalab.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.javalab.dto.EventDto;
import ru.itis.javalab.exception.AlreadyExistEventException;
import ru.itis.javalab.exception.NoFreeTimeException;
import ru.itis.javalab.exception.NoSuchEventException;
import ru.itis.javalab.exception.NoSuchUserException;
import ru.itis.javalab.form.EventForm;
import ru.itis.javalab.models.Event;
import ru.itis.javalab.models.User;
import ru.itis.javalab.repositories.EventRepository;
import ru.itis.javalab.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<EventDto> getFreeTime(EventForm eventForm) {
        List<User> users = checkExistingUsers(eventForm.getLogins());
        List<Event> usersFreeTime;
        List<Event> mergedFreeTime = new ArrayList<>();

        if (!users.isEmpty()) {
            for (User user : users) {
                usersFreeTime = eventRepository.getFreeTime(user.getId());
                if (!usersFreeTime.isEmpty()) {
                    if (mergedFreeTime.isEmpty()) {
                        mergedFreeTime.addAll(usersFreeTime);
                    } else {
                        mergedFreeTime = getMergedCommonSchedule(mergedFreeTime, usersFreeTime);
                    }
                } else {
                    mergedFreeTime = null;
                    break;
                }
            }
        } else {
            throw new NoSuchEventException("no such user");
        }
        if (mergedFreeTime == null){
            throw new NoFreeTimeException("no free time");
        } else {
            return EventDto.fromModelToDto(mergedFreeTime);
        }
    }

    @Override
    public List<User> checkExistingUsers(List<String> userLogins) {
        List<User> users = new ArrayList<>();
        for (String userLogin : userLogins) {
            Optional<User> user = userRepository.findByEmail(userLogin);
            if (user.isPresent()) {
                users.add(user.get());
            } else {
                users.clear();
                break;
            }
        }
        return users;
    }

    @Override
    public Optional<Event> findIntersection(List<Event> mergedFreeTime, Event event) {
        Event newEvent=null;
        LocalDateTime eventStarts = event.getEventStarts();
        LocalDateTime eventEnds = event.getEventEnds();
        for (int i=0; i<mergedFreeTime.size();i++){
            LocalDateTime freeTimeStarts = mergedFreeTime.get(i).getEventStarts();
            LocalDateTime freeTimeEnds = mergedFreeTime.get(i).getEventEnds();
            if (((eventStarts.isBefore(freeTimeStarts) || (eventStarts.isEqual(freeTimeStarts))) && eventStarts.isBefore(freeTimeEnds))
                    && (eventEnds.isAfter(freeTimeStarts) && (eventEnds.isBefore(freeTimeEnds) || eventEnds.isEqual(freeTimeEnds)))){
                newEvent = Event.builder()
                        .eventStarts(freeTimeStarts)
                        .eventEnds(eventEnds)
                        .build();
                break;
            } else if ( ((eventStarts.isAfter(freeTimeStarts) || eventStarts.isEqual(freeTimeStarts))
                    &&(eventStarts.isBefore(freeTimeEnds))) && (eventEnds.isAfter(freeTimeEnds))){
                newEvent = Event.builder()
                        .eventStarts(eventStarts)
                        .eventEnds(freeTimeEnds)
                        .build();
                break;
            } else if( eventStarts.isAfter(freeTimeStarts) && eventEnds.isBefore(freeTimeEnds)) {
                newEvent = Event.builder()
                        .eventStarts(eventStarts)
                        .eventEnds(eventEnds)
                        .build();
                break;
            } else if(eventStarts.isBefore(freeTimeStarts) && eventEnds.isAfter(freeTimeEnds)){
                newEvent = Event.builder()
                        .eventStarts(freeTimeStarts)
                        .eventEnds(freeTimeEnds)
                        .build();
                break;
            }
        }
        return Optional.ofNullable(newEvent);
    }

    @Override
    public List<Event> getMergedCommonSchedule(List<Event> mergedFreeTime, List<Event> usersFreeTime) {
        List<Event> newMergedFreeTime = new ArrayList<>();
        Optional<Event> intersectedEvent;
        for (Event event : usersFreeTime) {
            intersectedEvent = findIntersection(mergedFreeTime, event);
            intersectedEvent.ifPresent(newMergedFreeTime::add);
        }
        return newMergedFreeTime;
    }

    @Override
    public EventDto addEvent(EventForm eventForm) {

        Optional<User> userOptional = userRepository.findByEmail(eventForm.getLogin());

        if (userOptional.isPresent()) {
            User currentUser = userOptional.get();
            LocalDateTime currentTime = LocalDateTime.now();
            if (!isEventExist(LocalDateTime.parse(eventForm.getEventStarts()), LocalDateTime.parse(eventForm.getEventEnds()), currentUser)) {
                Event newEvent = Event.builder().eventName(eventForm.getName())
                        .addedTime(currentTime)
                        .eventStarts(LocalDateTime.parse(eventForm.getEventStarts()))
                        .eventEnds(LocalDateTime.parse(eventForm.getEventEnds()))
                        .user(currentUser)
                        .build();
                eventRepository.save(newEvent);
            } else {
                throw new NoSuchEventException("no such event");
            }
        } else {
            throw new NoSuchUserException("no such user");
        }
        return EventDto.fromFormToDto(eventForm);
    }

    @Override
    public Boolean isEventExist(LocalDateTime eventStarts, LocalDateTime eventEnds, User user) {
        return eventRepository.checkExistingEvent(eventStarts, eventEnds, user);
    }

    @Override
    public EventDto addEvents(EventForm eventForm) {
        List<User> users = checkExistingUsers(eventForm.getLogins());
        if (!users.isEmpty()) {
            LocalDateTime currentTime = LocalDateTime.now();
            for (User user : users) {
                if (!isEventExist(LocalDateTime.parse(eventForm.getEventStarts()),LocalDateTime.parse(eventForm.getEventEnds()), user)) {
                    throw new AlreadyExistEventException("event already exist");
                } else {
                    Event newEvent = Event.builder().eventName(eventForm.getName())
                            .addedTime(currentTime)
                            .eventStarts(LocalDateTime.parse(eventForm.getEventStarts()))
                            .eventEnds(LocalDateTime.parse(eventForm.getEventEnds()))
                            .user(user)
                            .build();
                    eventRepository.save(newEvent);
                }
            }
        } else {
            throw new NoSuchUserException("no such user");
        }
        return EventDto.fromFormToDto(eventForm);
    }
}
