package com.example.inventoryservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private final InventoryService inventoryService;

    public OrderEventConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "order-placed-topic", groupId = "inventory-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        System.out.println("<<< Received order event: " + event);
        boolean ok = inventoryService.decrementStock(event.itemId(), event.quantity());
        System.out.println(ok ? "Stock decremented OK" : "Stock decrement FAILED");
    }
}
