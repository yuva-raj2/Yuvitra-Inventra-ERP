package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryTransactionResponse {

    private Long id;

    private String productName;

    private String transactionType;

    private Integer quantity;

    private Integer previousStock;

    private Integer currentStock;

    private String remarks;
}