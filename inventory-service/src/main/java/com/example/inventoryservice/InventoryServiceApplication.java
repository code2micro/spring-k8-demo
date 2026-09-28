package com.example.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedInventory(InventoryRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new InventoryItem("item-1", 10));
                repo.save(new InventoryItem("item-2", 5));
                repo.save(new InventoryItem("item-3", 0));
                System.out.println(">>> Seeded initial inventory data");
            } else {
                System.out.println(">>> Inventory data already exists, skipping seed");
            }
        };
    }
}
