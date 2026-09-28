package com.example.inventoryservice;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_orders")
public class ProcessedOrder {

    @Id
    private String orderId;

    private LocalDateTime processedAt;

    public ProcessedOrder() {}

    public ProcessedOrder(String orderId, LocalDateTime processedAt) {
        this.orderId = orderId;
        this.processedAt = processedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
