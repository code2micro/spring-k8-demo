package com.example.springk8demo;

public record OrderPlacedEvent(String orderId, String itemId, int quantity) {}
