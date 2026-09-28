package com.example.inventoryservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final Map<String, Integer> stock = new ConcurrentHashMap<>();

    public InventoryService() {
        // Seed some initial stock for testing
        stock.put("item-1", 10);
        stock.put("item-2", 5);
        stock.put("item-3", 0);
    }

    public int getStock(String itemId) {
        return stock.getOrDefault(itemId, 0);
    }

    public void setStock(String itemId, int quantity) {
        stock.put(itemId, quantity);
    }

    public boolean decrementStock(String itemId, int quantity) {
        return stock.computeIfPresent(itemId, (id, current) -> {
            if (current < quantity) {
                return current; // not enough stock — leave unchanged
            }
            return current - quantity;
        }) != null && stock.get(itemId) < (stock.get(itemId) + quantity);
    }
}
