package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SalesOrderResponse {

    private Long id;

    private String orderNumber;

    private Long customerId;

    private String customerName;

    private String status;

    private BigDecimal totalAmount;

    private LocalDateTime orderDate;

    private List<SalesOrderItemResponse> items;
}