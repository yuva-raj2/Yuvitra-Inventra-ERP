package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SupplierPerformanceResponse {

    private String supplierName;
    private Long totalPurchaseOrders;
    private BigDecimal totalPurchaseAmount;
}