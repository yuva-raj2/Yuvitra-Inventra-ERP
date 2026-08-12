package com.yuvitra.inventory.repository;

import com.yuvitra.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);
    List<Product> findByProductNameContainingIgnoreCase(
            String productName);
    Optional<Product> findBySku(String sku);
    List<Product> findByQuantityLessThanEqual(
            Integer quantity);
    long countByQuantityLessThanEqual(Integer quantity);
}