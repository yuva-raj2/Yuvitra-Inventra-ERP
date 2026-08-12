package com.yuvitra.inventory.dto.request;

import lombok.Data;

@Data
public class StockTransactionRequest {

    private Long productId;

    private Integer quantity;

    private String remarks;
}