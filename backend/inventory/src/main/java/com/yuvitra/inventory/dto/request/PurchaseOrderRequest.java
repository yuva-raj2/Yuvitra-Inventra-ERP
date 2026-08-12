package com.yuvitra.inventory.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderRequest {

    private Long supplierId;

    private LocalDateTime expectedDeliveryDate;

    private List<PurchaseOrderItemRequest> items;
}