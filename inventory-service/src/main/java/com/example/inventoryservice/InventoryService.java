package com.example.inventoryservice;

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
    // Step 1: Try to record this orderId. If it exists, the DB throws.
    try {
        processedOrderRepo.saveAndFlush(new ProcessedOrder(orderId, LocalDateTime.now()));
    } catch (DataIntegrityViolationException e) {
        return ProcessResult.DUPLICATE;
    }

    // Step 2: Only reached on first-time processing
    boolean ok = decrementStock(itemId, quantity);
    return ok ? ProcessResult.PROCESSED : ProcessResult.OUT_OF_STOCK;
	}
	public enum ProcessResult {
    PROCESSED, DUPLICATE, OUT_OF_STOCK
}
}
