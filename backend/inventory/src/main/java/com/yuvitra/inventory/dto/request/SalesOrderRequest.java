package com.yuvitra.inventory.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SalesOrderRequest {

    private Long customerId;

    private List<SalesOrderItemRequest> items;
}