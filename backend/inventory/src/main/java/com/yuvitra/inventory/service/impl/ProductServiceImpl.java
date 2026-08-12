package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.request.ProductRequest;
import com.yuvitra.inventory.dto.response.ProductResponse;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.exception.DuplicateResourceException;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Override
    public List<ProductResponse> getLowStockProducts() {

        return productRepository
                .findByQuantityLessThanEqual(5)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .sku(product.getSku())
                        .category(product.getCategory())
                        .brand(product.getBrand())
                        .quantity(product.getQuantity())
                        .minimumStock(product.getMinimumStock())
                        .unitPrice(product.getUnitPrice())
                        .build())
                .toList();
    }
    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .sku(product.getSku())
                        .category(product.getCategory())
                        .brand(product.getBrand())
                        .quantity(product.getQuantity())
                        .minimumStock(product.getMinimumStock())
                        .unitPrice(product.getUnitPrice())
                        .build())
                .toList();
    }
    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .sku(product.getSku())
                .category(product.getCategory())
                .brand(product.getBrand())
                .quantity(product.getQuantity())
                .minimumStock(product.getMinimumStock())
                .unitPrice(product.getUnitPrice())
                .build();
    }
    @Override
    public ProductResponse updateProduct(
            Long id,
            ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"));

        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setQuantity(request.getQuantity());
        product.setMinimumStock(request.getMinimumStock());
        product.setUnitPrice(request.getUnitPrice());
        product.setDescription(request.getDescription());
        product.setUpdatedAt(LocalDateTime.now());

        Product updated =
                productRepository.save(product);

        return ProductResponse.builder()
                .id(updated.getId())
                .productName(updated.getProductName())
                .sku(updated.getSku())
                .category(updated.getCategory())
                .brand(updated.getBrand())
                .quantity(updated.getQuantity())
                .minimumStock(updated.getMinimumStock())
                .unitPrice(updated.getUnitPrice())
                .build();
    }
    @Override
    public String deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"));

        productRepository.delete(product);

        return "Product Deleted Successfully";
    }
    @Override
    public List<ProductResponse> searchProducts(
            String productName) {

        return productRepository
                .findByProductNameContainingIgnoreCase(productName)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .sku(product.getSku())
                        .category(product.getCategory())
                        .brand(product.getBrand())
                        .quantity(product.getQuantity())
                        .minimumStock(product.getMinimumStock())
                        .unitPrice(product.getUnitPrice())
                        .build())
                .toList();
    }
}