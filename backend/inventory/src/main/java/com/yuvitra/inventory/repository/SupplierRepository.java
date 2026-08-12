package com.yuvitra.inventory.repository;

import com.yuvitra.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository
        extends JpaRepository<Supplier, Long> {

    List<Supplier>
    findBySupplierNameContainingIgnoreCase(
            String supplierName);
}
