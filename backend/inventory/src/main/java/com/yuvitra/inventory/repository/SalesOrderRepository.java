package com.yuvitra.inventory.repository;

import com.yuvitra.inventory.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesOrderRepository
        extends JpaRepository<SalesOrder, Long> {

    Optional<SalesOrder>
    findByOrderNumber(String orderNumber);
}