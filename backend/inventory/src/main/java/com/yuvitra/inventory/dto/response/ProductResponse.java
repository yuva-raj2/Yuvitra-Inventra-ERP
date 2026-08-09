package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {

    private Long id;
    private String productName;
    private String sku;
    private String category;
    private String brand;
    private Integer quantity;
    private Integer minimumStock;
    private BigDecimal unitPrice;
}