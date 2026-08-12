package com.yuvitra.inventory.repository;

import com.yuvitra.inventory.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long> {

    List<InventoryTransaction>
    findByProductIdOrderByTransactionDateDesc(
            Long productId);
}