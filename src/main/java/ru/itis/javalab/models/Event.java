package ru.itis.javalab.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.javalab.dto.EventDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = {@Index(name = "eventStarts", columnList = "eventStarts"),
                    @Index(name="eventEnds", columnList = "eventEnds")})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventName;
    private LocalDateTime addedTime;
    private LocalDateTime eventStarts;
    private LocalDateTime eventEnds;
    @ManyToOne
    private User user;

}
