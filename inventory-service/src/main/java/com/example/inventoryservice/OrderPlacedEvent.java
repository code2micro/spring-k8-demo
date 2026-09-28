package com.example.inventoryservice;

public record OrderPlacedEvent(String orderId, String itemId, int quantity) {}
