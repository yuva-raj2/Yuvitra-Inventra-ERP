package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PurchaseOrderResponse {

    private Long id;

    private String orderNumber;

    private Long supplierId;

    private String supplierName;

    private String status;

    private BigDecimal totalAmount;

    private LocalDateTime orderDate;

    private LocalDateTime expectedDeliveryDate;

    private List<PurchaseOrderItemResponse> items;
}