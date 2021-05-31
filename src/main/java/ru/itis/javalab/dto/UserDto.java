package ru.itis.javalab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.javalab.form.UserForm;
import ru.itis.javalab.models.User;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String name;
    private String login;

    public static UserDto fromModelToDto(User user){
        return UserDto.builder().login(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> fromModelToDto(List<User> users){
       return users.stream().map(UserDto::fromModelToDto).collect(Collectors.toList());
    }

    public static UserDto fromFormToDto(UserForm userForm){
        return UserDto.builder().login(userForm.getLogin())
                .name(userForm.getName())
                .build();
    }

    public static List<UserDto> fromFormToDto(List<UserForm> userForms){
        return userForms.stream().map(UserDto::fromFormToDto).collect(Collectors.toList());
    }

}
