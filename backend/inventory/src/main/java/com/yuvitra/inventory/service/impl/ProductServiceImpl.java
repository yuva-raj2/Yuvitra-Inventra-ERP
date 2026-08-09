package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.request.ProductRequest;
import com.yuvitra.inventory.dto.response.ProductResponse;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.exception.DuplicateResourceException;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException(
                    "SKU already exists");
        }

        Product product = Product.builder()
                .productName(request.getProductName())
                .sku(request.getSku())
                .category(request.getCategory())
                .brand(request.getBrand())
                .quantity(request.getQuantity())
                .minimumStock(request.getMinimumStock())
                .unitPrice(request.getUnitPrice())
                .description(request.getDescription())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product saved = productRepository.save(product);

        return ProductResponse.builder()
                .id(saved.getId())
                .productName(saved.getProductName())
                .sku(saved.getSku())
                .category(saved.getCategory())
                .brand(saved.getBrand())
                .quantity(saved.getQuantity())
                .minimumStock(saved.getMinimumStock())
                .unitPrice(saved.getUnitPrice())
                .build();
    }
}