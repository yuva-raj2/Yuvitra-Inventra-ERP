package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InventorySummaryResponse {

    private Long totalProducts;

    private Integer totalStock;

    private BigDecimal inventoryValue;

    private Long lowStockProducts;
}