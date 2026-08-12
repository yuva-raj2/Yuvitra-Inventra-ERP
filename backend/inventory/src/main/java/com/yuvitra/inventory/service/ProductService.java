package com.yuvitra.inventory.service;

import com.yuvitra.inventory.dto.request.ProductRequest;
import com.yuvitra.inventory.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    // Create
    ProductResponse createProduct(
            ProductRequest request);

    // Get All
    List<ProductResponse> getAllProducts();

    // Get By ID
    ProductResponse getProductById(Long id);

    // Update
    ProductResponse updateProduct(
            Long id,
            ProductRequest request);

    // Delete
    String deleteProduct(Long id);

    // Search
    List<ProductResponse> searchProducts(
            String productName);

    // Low Stock
    List<ProductResponse> getLowStockProducts();
}