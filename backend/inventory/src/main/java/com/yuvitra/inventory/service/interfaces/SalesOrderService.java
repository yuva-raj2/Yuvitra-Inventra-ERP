package com.yuvitra.inventory.service.interfaces;

import com.yuvitra.inventory.dto.request.SalesOrderRequest;
import com.yuvitra.inventory.dto.response.SalesOrderResponse;
import com.yuvitra.inventory.entity.enums.SalesOrderStatus;

import java.util.List;

public interface SalesOrderService {

    SalesOrderResponse createSalesOrder(
            SalesOrderRequest request);

    List<SalesOrderResponse> getAllSalesOrders();

    SalesOrderResponse getSalesOrderById(
            Long id);

    SalesOrderResponse updateStatus(
            Long id,
            SalesOrderStatus status);
}