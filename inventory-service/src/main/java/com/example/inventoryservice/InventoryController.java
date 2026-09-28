package com.example.inventoryservice;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public String getInventory() {
        return "Inventory Service";
    }

    @GetMapping("/inventory/{itemId}")
    public Map<String, Object> getStock(@PathVariable String itemId) {
        return Map.of("itemId", itemId, "stock", inventoryService.getStock(itemId));
    }

    @PostMapping("/inventory/{itemId}/stock/{qty}")
    public Map<String, Object> setStock(@PathVariable String itemId, @PathVariable int qty) {
        inventoryService.setStock(itemId, qty);
        return Map.of("itemId", itemId, "stock", inventoryService.getStock(itemId));
    }
}

