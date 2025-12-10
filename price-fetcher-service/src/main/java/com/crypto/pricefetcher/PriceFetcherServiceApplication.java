package com.crypto.pricefetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceFetcherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceFetcherServiceApplication.class, args);
	}
}
