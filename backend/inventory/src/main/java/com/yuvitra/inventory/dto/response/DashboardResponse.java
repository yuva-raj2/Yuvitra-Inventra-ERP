package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private Long totalProducts;

    private Long totalStockQuantity;

    private Long lowStockProducts;

    private Double totalInventoryValue;
}