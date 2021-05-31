package ru.itis.javalab.services;

import ru.itis.javalab.dto.UserDto;
import ru.itis.javalab.form.UserForm;

public interface UserService {
    UserDto addUser(UserForm userForm);
}
