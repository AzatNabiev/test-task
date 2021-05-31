package ru.itis.javalab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.itis.javalab.form.EventForm;
import ru.itis.javalab.models.Event;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private String login;
    private List<String> logins;
    private String name;
    private LocalDateTime eventStarts;
    private LocalDateTime eventEnds;

    public static EventDto fromModelToDto(Event event){
        return EventDto.builder()
                .name(event.getEventName())
                .eventStarts(event.getEventStarts())
                .eventEnds(event.getEventEnds())
                .build();
    }

    public static List<EventDto> fromModelToDto(List<Event> events){
        return events.stream().map(EventDto::fromModelToDto).collect(Collectors.toList());
    }

    public static EventDto fromFormToDto(EventForm eventForm){
        return EventDto.builder()
                .name(eventForm.getName())
                .login(eventForm.getLogin())
                .logins(eventForm.getLogins())
                .eventStarts(LocalDateTime.parse(eventForm.getEventStarts()))
                .eventEnds(LocalDateTime.parse(eventForm.getEventEnds()))
                .build();
    }

    public static List<EventDto> from(List<EventForm> events){
        return events.stream().map(EventDto::fromFormToDto).collect(Collectors.toList());
    }


}
