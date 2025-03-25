package com.dotnt.cinemaback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CinemaBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaBackApplication.class, args);
    }

}
