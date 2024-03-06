package com.example.nms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

@Configuration
public class TestConfiguration {

    @Bean
    Faker faker() {
        return new Faker();
    }

}
