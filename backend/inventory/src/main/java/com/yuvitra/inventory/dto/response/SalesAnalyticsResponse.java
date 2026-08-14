package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SalesAnalyticsResponse {

    private Long totalSalesOrders;

    private Long completedSalesOrders;

    private Long pendingSalesOrders;

    private BigDecimal totalRevenue;
}