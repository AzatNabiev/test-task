package ru.itis.javalab.form;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventForm {
    @Email
    private String login;
    private List<@Email String> logins;
    private String name;
    private String eventStarts;
    private String eventEnds;
}
