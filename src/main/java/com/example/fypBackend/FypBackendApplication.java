package com.example.fypBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("application.properties")
public class FypBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FypBackendApplication.class, args);
	}

}
