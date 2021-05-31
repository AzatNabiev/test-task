package ru.itis.javalab.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.itis.javalab.dto.UserDto;
import ru.itis.javalab.exception.IncorrectGivenData;
import ru.itis.javalab.form.UserForm;
import ru.itis.javalab.services.UserService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName(("UserAddController is working when"))
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        UserForm testUser = UserForm.builder()
                .login("test@gmail.com")
                .name("Azat")
                .build();
        when(userService.addUser(testUser)).thenReturn(UserDto.builder()
                .login("test@gmail.com")
                .name("Azat")
                .build());

        doThrow(new IncorrectGivenData("Incorrect given data"))
                .when(userService)
                .addUser(UserForm.builder()
                        .login("testtest").build());
    }

    @Nested
    @DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("addUser() is working")
    class UserAdding {
        @Test
        public void add_user_test() throws Exception {
            String jsonVal = "{\n" +
                    "  \"login\": \"test@gmail.com\",\n" +
                    "  \"name\": \"Azat\"\n" +
                    "}";
            mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonVal)
                    .characterEncoding("utf-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Azat")))
                    .andExpect(jsonPath("$.login", is("test@gmail.com")))
                    .andReturn();
        }

        @Test
        public void throws_exception_for_incorrect_email() throws Exception {
            mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"login\": \"testgmail.com\",\n" +
                            "  \"name\": \"Daniil\"\n" +
                            "}"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }
    }
}
