package com.yuvitra.inventory.service;

import com.yuvitra.inventory.dto.request.StockTransactionRequest;
import com.yuvitra.inventory.dto.response.InventoryTransactionResponse;

import java.util.List;

public interface InventoryTransactionService {

    InventoryTransactionResponse stockIn(
            StockTransactionRequest request);

    InventoryTransactionResponse stockOut(
            StockTransactionRequest request);

    List<InventoryTransactionResponse>
    getTransactionHistory(Long productId);
}