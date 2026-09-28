package com.example.inventoryservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
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
}
