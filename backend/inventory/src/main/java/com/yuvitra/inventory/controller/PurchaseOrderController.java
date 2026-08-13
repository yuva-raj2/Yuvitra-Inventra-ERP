package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.dto.request.PurchaseOrderRequest;
import com.yuvitra.inventory.dto.response.PurchaseOrderResponse;
import com.yuvitra.inventory.service.interfaces.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.yuvitra.inventory.dto.request.ReceivePurchaseOrderRequest;
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public PurchaseOrderResponse createPurchaseOrder(
            @RequestBody PurchaseOrderRequest request) {

        return purchaseOrderService
                .createPurchaseOrder(request);
    }

    @GetMapping
    public List<PurchaseOrderResponse>
    getAllPurchaseOrders() {

        return purchaseOrderService
                .getAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public PurchaseOrderResponse
    getPurchaseOrderById(
            @PathVariable Long id) {

        return purchaseOrderService
                .getPurchaseOrderById(id);
    }

    @PatchMapping("/{id}/status")
    public PurchaseOrderResponse updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return purchaseOrderService
                .updateStatus(id, status);
    }
    @PostMapping("/{id}/receive")
    public PurchaseOrderResponse receivePurchaseOrder(
            @PathVariable Long id,
            @RequestBody
            ReceivePurchaseOrderRequest request) {

        return purchaseOrderService
                .receivePurchaseOrder(id, request);
    }
}