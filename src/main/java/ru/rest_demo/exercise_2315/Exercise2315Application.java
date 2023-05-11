package ru.rest_demo.exercise_2315;


import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Exercise2315Application {

    public static void main(String[] args) {
        SpringApplication.run(Exercise2315Application.class, args);
    }

    @Bean
    ModelMapper modelMapper() { return new ModelMapper(); }

}
