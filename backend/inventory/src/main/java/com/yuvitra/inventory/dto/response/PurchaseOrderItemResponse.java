package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PurchaseOrderItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
    private Integer receivedQuantity;

    private Integer remainingQuantity;
}