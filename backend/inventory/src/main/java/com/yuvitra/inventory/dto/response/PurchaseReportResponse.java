package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PurchaseReportResponse {

    private Long purchaseOrderId;
    private String purchaseOrderNumber;
    private String supplierName;
    private String status;

    private BigDecimal totalAmount;

    private Integer totalItems;
}