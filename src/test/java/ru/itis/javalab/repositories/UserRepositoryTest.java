package ru.itis.javalab.repositories;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.itis.javalab.models.User;
import static junit.framework.TestCase.assertEquals;
import java.util.Optional;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES, beanName = "dataSource", provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void return_correct_user() {
        User expectedUser = User.builder()
                .id(1L)
                .email("testmail@gmail.com")
                .name("Azat")
                .build();
        Optional<User> actualUser = userRepository.findByEmail("testmail@gmail.com");
        actualUser.ifPresent(val -> assertEquals(expectedUser,val));
    }

    @Test
    void return_incorrect_user(){
        User expectedUser = null;
        Optional<User> actualUser = userRepository.findByEmail("testerrormail@gmail.com");
        actualUser.ifPresent(val -> assertEquals(expectedUser,val));
    }


}
