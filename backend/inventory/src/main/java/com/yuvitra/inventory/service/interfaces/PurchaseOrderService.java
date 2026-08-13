package com.yuvitra.inventory.service.interfaces;

import com.yuvitra.inventory.dto.request.PurchaseOrderRequest;
import com.yuvitra.inventory.dto.request.ReceivePurchaseOrderRequest;
import com.yuvitra.inventory.dto.response.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrderResponse createPurchaseOrder(
            PurchaseOrderRequest request);

    List<PurchaseOrderResponse> getAllPurchaseOrders();

    PurchaseOrderResponse getPurchaseOrderById(
            Long id);

    PurchaseOrderResponse updateStatus(
            Long id,
            String status);
    PurchaseOrderResponse receivePurchaseOrder(
            Long id,
            ReceivePurchaseOrderRequest request);
}