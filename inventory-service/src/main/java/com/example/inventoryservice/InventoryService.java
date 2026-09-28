package com.example.inventoryservice;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository repo;
    private final ProcessedOrderRepository processedOrderRepo;

    public InventoryService(InventoryRepository repo, ProcessedOrderRepository processedOrderRepo) {
        this.repo = repo;
        this.processedOrderRepo = processedOrderRepo;
    }

    public int getStock(String itemId) {
        return repo.findById(itemId).map(InventoryItem::getQuantity).orElse(0);
    }

    public void setStock(String itemId, int quantity) {
        repo.save(new InventoryItem(itemId, quantity));
    }

    @Transactional
    public boolean decrementStock(String itemId, int quantity) {
        return repo.findById(itemId).map(item -> {
            if (item.getQuantity() < quantity) {
                return false;
            }
            item.setQuantity(item.getQuantity() - quantity);
            repo.save(item);
            return true;
        }).orElse(false);
    }

    @Transactional
    public ProcessResult processOrder(String orderId, String itemId, int quantity) {
        if (processedOrderRepo.existsById(orderId)) {
            return ProcessResult.DUPLICATE;
        }
        processedOrderRepo.save(new ProcessedOrder(orderId, LocalDateTime.now()));
        boolean ok = decrementStock(itemId, quantity);
        return ok ? ProcessResult.PROCESSED : ProcessResult.OUT_OF_STOCK;
    }
}
