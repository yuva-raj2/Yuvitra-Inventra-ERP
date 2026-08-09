package com.yuvitra.inventory.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    private String productName;
    private String sku;
    private String category;
    private String brand;
    private Integer quantity;
    private Integer minimumStock;
    private BigDecimal unitPrice;
    private String description;
}