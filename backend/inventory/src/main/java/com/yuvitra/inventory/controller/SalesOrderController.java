package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.dto.request.SalesOrderRequest;
import com.yuvitra.inventory.dto.response.SalesOrderResponse;
import com.yuvitra.inventory.entity.enums.SalesOrderStatus;
import com.yuvitra.inventory.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public SalesOrderResponse createSalesOrder(
            @RequestBody SalesOrderRequest request) {

        return salesOrderService
                .createSalesOrder(request);
    }

    @GetMapping
    public List<SalesOrderResponse>
    getAllSalesOrders() {

        return salesOrderService
                .getAllSalesOrders();
    }

    @GetMapping("/{id}")
    public SalesOrderResponse getSalesOrderById(
            @PathVariable Long id) {

        return salesOrderService
                .getSalesOrderById(id);
    }

    @PatchMapping("/{id}/status")
    public SalesOrderResponse updateStatus(
            @PathVariable Long id,
            @RequestParam SalesOrderStatus status){

        return salesOrderService
                .updateStatus(id, status);
    }
}