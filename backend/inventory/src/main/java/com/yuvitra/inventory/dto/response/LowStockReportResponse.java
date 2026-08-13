package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LowStockReportResponse {

    private Long productId;

    private String productName;

    private String sku;

    private String category;

    private Integer quantity;

    private Integer minimumStock;

    private Integer shortage;

    private BigDecimal unitPrice;

    private BigDecimal reorderValue;
}