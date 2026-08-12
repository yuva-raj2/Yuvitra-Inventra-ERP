package com.yuvitra.inventory.repository;

import com.yuvitra.inventory.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder>
    findByOrderNumber(String orderNumber);
}