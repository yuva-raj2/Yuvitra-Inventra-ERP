package com.yuvitra.inventory.service;

import com.yuvitra.inventory.dto.request.ProductRequest;
import com.yuvitra.inventory.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(
            ProductRequest request);
}