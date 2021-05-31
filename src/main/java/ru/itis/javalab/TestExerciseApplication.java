package ru.itis.javalab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TestExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestExerciseApplication.class, args);
    }

}
