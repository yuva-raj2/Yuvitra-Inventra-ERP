package com.yuvitra.inventory.dto.request;

import lombok.Data;

@Data
public class ReceivePurchaseOrderItemRequest {

    private Long purchaseOrderItemId;

    private Integer quantity;
}