package ru.itis.javalab.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = {"events"})
@EqualsAndHashCode(exclude = {"events"})
@Table(name = "account",indexes = {@Index(name = "email", columnList = "email", unique = true)})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Event> events;
}
