package com.example.ddd_start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class DddStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(DddStartApplication.class, args);
	}

}
