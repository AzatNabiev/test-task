package ru.itis.javalab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.javalab.dto.EventDto;
import ru.itis.javalab.form.EventForm;
import ru.itis.javalab.services.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

    private EventService eventService;

    @Autowired
    public EventsController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping("/time/free")
    public ResponseEntity<List<EventDto>> getFreeTime(@Valid @RequestBody EventForm eventForm){
        List<EventDto> events = eventService.getFreeTime(eventForm);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/several")
    public ResponseEntity<EventDto> addEvents(@Valid @RequestBody EventForm eventForm){
        return ResponseEntity.ok(eventService.addEvents(eventForm));
    }

    @PostMapping
    public ResponseEntity<EventDto> addEvent(@Valid @RequestBody EventForm eventForm){
        return ResponseEntity.ok(eventService.addEvent(eventForm));
    }


}
