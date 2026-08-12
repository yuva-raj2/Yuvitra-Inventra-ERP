package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.dto.request.StockTransactionRequest;
import com.yuvitra.inventory.dto.response.InventoryTransactionResponse;
import com.yuvitra.inventory.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final
    InventoryTransactionService
            inventoryTransactionService;

    @PostMapping("/stock-in")
    public InventoryTransactionResponse
    stockIn(
            @RequestBody
            StockTransactionRequest request) {

        return inventoryTransactionService
                .stockIn(request);
    }

    @PostMapping("/stock-out")
    public InventoryTransactionResponse
    stockOut(
            @RequestBody
            StockTransactionRequest request) {

        return inventoryTransactionService
                .stockOut(request);
    }

    @GetMapping("/history/{productId}")
    public List<InventoryTransactionResponse>
    getHistory(
            @PathVariable Long productId) {

        return inventoryTransactionService
                .getTransactionHistory(
                        productId);
    }
}
