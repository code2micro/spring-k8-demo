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

		ProcessResult result = inventoryService.processOrder(
			event.orderId(),
			event.itemId(),
			event.quantity()
		);

		switch (result) {
			case PROCESSED   -> System.out.println("Stock decremented OK");
			case DUPLICATE   -> System.out.println("Skipped duplicate order: " + event.orderId());
			case OUT_OF_STOCK -> System.out.println("Insufficient stock for: " + event.itemId());
		}
}
}
