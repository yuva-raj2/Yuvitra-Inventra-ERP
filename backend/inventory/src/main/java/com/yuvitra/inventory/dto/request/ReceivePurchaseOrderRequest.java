package com.yuvitra.inventory.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ReceivePurchaseOrderRequest {

    private List<ReceivePurchaseOrderItemRequest> items;
}