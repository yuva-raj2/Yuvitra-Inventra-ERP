package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SalesReportResponse {

    private String salesOrderNumber;
    private String customerName;
    private String status;
    private BigDecimal totalAmount;
}