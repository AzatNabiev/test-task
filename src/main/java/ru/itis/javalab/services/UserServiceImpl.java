package ru.itis.javalab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.itis.javalab.dto.UserDto;
import ru.itis.javalab.exception.AlreadyExistUserException;
import ru.itis.javalab.form.UserForm;
import ru.itis.javalab.models.User;
import ru.itis.javalab.repositories.UserRepository;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"USERS"})
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @CachePut(key = "#userForm.login", unless = "#result == null")
    public UserDto addUser(UserForm userForm) {
        User user = User.builder().email(userForm.getLogin())
                .name(userForm.getName())
                .build();
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new AlreadyExistUserException("User already exists");
        }
        return UserDto.fromFormToDto(userForm);
    }

    @Override
    @Cacheable(unless = "#result == null")
    public List<UserDto> getUsers() {
        return UserDto.fromModelToDto(userRepository.findAll());
    }

    @Override
    @CacheEvict(key="#userForm.login")
    public UserDto updateUser(UserForm userForm) {
        //example for caching
        return null;
    }
}
