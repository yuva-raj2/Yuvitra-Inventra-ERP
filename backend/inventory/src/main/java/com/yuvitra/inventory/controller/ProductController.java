package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.dto.request.ProductRequest;
import com.yuvitra.inventory.dto.response.ProductResponse;
import com.yuvitra.inventory.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // CREATE PRODUCT
    @PostMapping
    public ProductResponse createProduct(
            @RequestBody ProductRequest request) {

        return productService.createProduct(request);
    }

    // GET ALL PRODUCTS
    @GetMapping
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }

    // SEARCH PRODUCTS BY NAME
    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam String productName) {

        return productService.searchProducts(productName);
    }

    // LOW STOCK PRODUCTS
    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts() {

        return productService.getLowStockProducts();
    }

    // GET PRODUCT BY ID
    @GetMapping("/id/{id}")
    public ProductResponse getProductById(
            @PathVariable Long id) {

        return productService.getProductById(id);
    }

    // UPDATE PRODUCT
    @PutMapping("/id/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request) {

        return productService.updateProduct(id, request);
    }

    // DELETE PRODUCT
    @DeleteMapping("/id/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        return productService.deleteProduct(id);
    }
}