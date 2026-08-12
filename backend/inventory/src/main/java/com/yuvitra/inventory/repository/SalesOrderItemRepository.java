package com.yuvitra.inventory.repository;

import com.yuvitra.inventory.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOrderItemRepository
        extends JpaRepository<SalesOrderItem, Long> {

    List<SalesOrderItem>
    findBySalesOrderId(Long salesOrderId);
}