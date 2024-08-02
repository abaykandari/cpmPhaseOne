package com.incture.cpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CpmApplication {
	public static void main(String[] args) {
		SpringApplication.run(CpmApplication.class, args);
	}
}
