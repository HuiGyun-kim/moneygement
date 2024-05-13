package com.room7.moneygement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneygementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneygementApplication.class, args);
	}

}
