package com.pdas.priCart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PriCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriCartApplication.class, args);
	}

}
