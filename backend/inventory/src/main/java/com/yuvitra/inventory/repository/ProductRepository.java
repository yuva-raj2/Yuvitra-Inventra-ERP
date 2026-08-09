package com.yuvitra.inventory.repository;

import com.yuvitra.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);
}