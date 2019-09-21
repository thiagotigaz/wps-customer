package com.wpspublish.customer;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	// Used to map DTO to Entity and vice-versa
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
