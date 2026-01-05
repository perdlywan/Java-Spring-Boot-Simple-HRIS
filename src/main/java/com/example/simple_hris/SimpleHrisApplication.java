package com.example.simple_hris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class SimpleHrisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleHrisApplication.class, args);
	}

}
