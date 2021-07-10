package ru.itis.javalab.services;

import ru.itis.javalab.dto.UserDto;
import ru.itis.javalab.form.UserForm;
import ru.itis.javalab.models.User;

import java.util.List;

public interface UserService {
    UserDto addUser(UserForm userForm);
    List<UserDto> getUsers();
    UserDto updateUser(UserForm userForm);
}
