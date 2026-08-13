package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.response.DashboardResponse;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final ProductRepository productRepository;

    @Override
    public DashboardResponse getDashboard() {

        long totalProducts =
                productRepository.count();

        long lowStockProducts =
                productRepository
                        .countByQuantityLessThanEqual(5);

        long totalStockQuantity =
                productRepository.findAll()
                        .stream()
                        .mapToLong(Product::getQuantity)
                        .sum();

        double inventoryValue =
                productRepository.findAll()
                        .stream()
                        .map(product ->
                                product.getUnitPrice()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        product.getQuantity()
                                                )))
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add)
                        .doubleValue();
        return DashboardResponse.builder()
                .totalProducts(totalProducts)
                .totalStockQuantity(
                        totalStockQuantity)
                .lowStockProducts(
                        lowStockProducts)
                .totalInventoryValue(
                        inventoryValue)
                .build();
    }
}