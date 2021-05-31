package ru.itis.javalab.smokes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itis.javalab.controllers.EventsController;
import ru.itis.javalab.controllers.UsersController;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class SmokeTest {
    @Autowired
    private EventsController eventsController;
    @Autowired
    private UsersController usersController;


    @Test
    void contextLoadsEventsController(){
        assertThat(eventsController, is(notNullValue()));
    }
    @Test
    void contextLoadsUsersController(){
        assertThat(usersController, is(notNullValue()));
    }

}
