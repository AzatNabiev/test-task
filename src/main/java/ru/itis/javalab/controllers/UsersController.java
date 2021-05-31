package ru.itis.javalab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.javalab.dto.UserDto;
import ru.itis.javalab.form.UserForm;
import ru.itis.javalab.services.UserService;

import javax.validation.Valid;

@RestController
public class UsersController {
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserForm userForm) {
        return ResponseEntity.ok(userService.addUser(userForm));
    }
}
