package com.example.springk8demo;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderEventPublisher eventPublisher;

    public OrderController(OrderEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/orders")
    public String getOrders() {
        String podName = System.getenv("HOSTNAME");
        System.out.println(">>> /orders handled by pod: " + podName);
        return "Order Service (pod: " + podName + ")";
    }

    @PostMapping("/orders")
    public Map<String, String> placeOrder(@RequestBody Map<String, Object> body) {
        String itemId = (String) body.get("itemId");
        int quantity = ((Number) body.getOrDefault("quantity", 1)).intValue();

        OrderPlacedEvent event = new OrderPlacedEvent(
            UUID.randomUUID().toString(),
            itemId,
            quantity
        );

        eventPublisher.publishOrderPlaced(event);

        return Map.of(
            "status", "PUBLISHED",
            "orderId", event.orderId(),
            "itemId", itemId
        );
    }
}
