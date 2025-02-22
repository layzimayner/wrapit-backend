package com.wrap.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		System.out.println("Driver Name: " + System.getenv("DATA_SOURCE_DRIVER_NAME"));
		SpringApplication.run(Application.class, args);
	}

}
