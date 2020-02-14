package com.devoxx.inventory;

import com.devoxx.inventory.domain.BookIdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

    @Bean
    public BookIdGenerator bookIdGenerator() {
        return () -> UUID.randomUUID().toString();
    }

}
