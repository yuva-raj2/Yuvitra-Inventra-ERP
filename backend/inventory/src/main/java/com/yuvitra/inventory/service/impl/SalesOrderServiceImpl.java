package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.request.SalesOrderItemRequest;
import com.yuvitra.inventory.dto.request.SalesOrderRequest;
import com.yuvitra.inventory.dto.request.StockTransactionRequest;
import com.yuvitra.inventory.dto.response.SalesOrderItemResponse;
import com.yuvitra.inventory.dto.response.SalesOrderResponse;
import com.yuvitra.inventory.entity.*;
import com.yuvitra.inventory.entity.enums.SalesOrderStatus;
import com.yuvitra.inventory.entity.enums.TransactionType;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.repository.*;
import com.yuvitra.inventory.service.InventoryTransactionService;
import com.yuvitra.inventory.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl
        implements SalesOrderService {
    private final InventoryTransactionRepository
            inventoryTransactionRepository;
    private final SalesOrderRepository
            salesOrderRepository;

    private final SalesOrderItemRepository
            salesOrderItemRepository;

    private final CustomerRepository
            customerRepository;

    private final ProductRepository
            productRepository;

    private final InventoryTransactionService
            inventoryTransactionService;

    @Override
    @Transactional
    public SalesOrderResponse createSalesOrder(
            SalesOrderRequest request) {

        Customer customer =
                customerRepository.findById(
                                request.getCustomerId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()));

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new IllegalArgumentException(
                    "Sales order must contain at least one item");
        }

        String orderNumber =
                generateOrderNumber();

        SalesOrder salesOrder =
                SalesOrder.builder()
                        .orderNumber(orderNumber)
                        .customer(customer)
                        .status(
                                SalesOrderStatus.DRAFT)
                        .totalAmount(
                                BigDecimal.ZERO)
                        .orderDate(
                                LocalDateTime.now())
                        .createdAt(
                                LocalDateTime.now())
                        .updatedAt(
                                LocalDateTime.now())
                        .build();

        SalesOrder savedOrder =
                salesOrderRepository.save(
                        salesOrder);

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (SalesOrderItemRequest itemRequest :
                request.getItems()) {

            Product product =
                    productRepository.findById(
                                    itemRequest.getProductId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found with id: "
                                                    + itemRequest
                                                    .getProductId()));

            if (itemRequest.getQuantity() == null ||
                    itemRequest.getQuantity() <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero");
            }

            if (itemRequest.getUnitPrice() == null ||
                    itemRequest.getUnitPrice()
                            .compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "Unit price must be greater than zero");
            }

            BigDecimal itemTotal =
                    itemRequest.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest
                                                    .getQuantity()));

            SalesOrderItem item =
                    SalesOrderItem.builder()
                            .salesOrder(savedOrder)
                            .product(product)
                            .quantity(
                                    itemRequest.getQuantity())
                            .unitPrice(
                                    itemRequest.getUnitPrice())
                            .totalPrice(itemTotal)
                            .build();

            salesOrderItemRepository.save(item);

            totalAmount =
                    totalAmount.add(itemTotal);
        }

        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setUpdatedAt(
                LocalDateTime.now());

        SalesOrder updated =
                salesOrderRepository.save(
                        savedOrder);

        return mapToResponse(updated);
    }
    @Override
    @Transactional
    public SalesOrderResponse updateStatus(
            Long id,
            SalesOrderStatus status) {

        SalesOrder order =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sales Order Not Found"));

        /*
         * Prevent duplicate confirmation
         */
        if(order.getStatus() ==
                SalesOrderStatus.CONFIRMED
                &&
                status ==
                        SalesOrderStatus.CONFIRMED){

            throw new RuntimeException(
                    "Order Already Confirmed");
        }

        /*
         * Stock deduction
         */
        if(status ==
                SalesOrderStatus.CONFIRMED){

            deductStock(order);
        }

        order.setStatus(status);

        SalesOrder updated =
                salesOrderRepository.save(order);

        return mapToResponse(updated);
    }

    private void deductStock(
            SalesOrder order){

        List<SalesOrderItem> items =
                salesOrderItemRepository
                        .findBySalesOrderId(
                                order.getId());

        for(SalesOrderItem item : items){

            Product product =
                    item.getProduct();

            /*
             * Validation
             */
            if(product.getQuantity()
                    <
                    item.getQuantity()){

                throw new RuntimeException(
                        "Insufficient stock for "
                                +
                                product.getProductName());
            }

            Integer oldQty =
                    product.getQuantity();

            Integer newQty =
                    oldQty -
                            item.getQuantity();

            product.setQuantity(newQty);

            productRepository.save(product);

            /*
             * Inventory Transaction
             */
            InventoryTransaction transaction =
                    InventoryTransaction.builder()
                            .product(product)
                            .transactionType(
                                    TransactionType.OUT)
                            .quantity(
                                    item.getQuantity())
                            .remarks(
                                    "Sales Order : "
                                            +
                                            order.getOrderNumber())
                            .createdAt(
                                    LocalDateTime.now())
                            .build();

            inventoryTransactionRepository
                    .save(transaction);
        }
    }

    @Override
    public List<SalesOrderResponse>
    getAllSalesOrders() {

        return salesOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public SalesOrderResponse
    getSalesOrderById(Long id) {

        SalesOrder salesOrder =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sales order not found with id: "
                                                + id));

        return mapToResponse(salesOrder);
    }

    @Override
    @Transactional
    public SalesOrderResponse updateStatus(
            Long id,
            String status) {

        SalesOrder salesOrder =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sales order not found with id: "
                                                + id));

        SalesOrderStatus newStatus;

        try {

            newStatus =
                    SalesOrderStatus.valueOf(
                            status.toUpperCase());

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid sales order status: "
                            + status);
        }

        /*
         * Stock is reduced only when the order
         * is confirmed for the first time.
         */
        if (newStatus == SalesOrderStatus.CONFIRMED &&
                salesOrder.getStatus()
                        != SalesOrderStatus.CONFIRMED) {

            List<SalesOrderItem> items =
                    salesOrderItemRepository
                            .findBySalesOrderId(id);

            for (SalesOrderItem item : items) {

                Product product =
                        item.getProduct();

                Integer currentStock =
                        product.getQuantity() == null
                                ? 0
                                : product.getQuantity();

                if (currentStock <
                        item.getQuantity()) {

                    throw new IllegalArgumentException(
                            "Insufficient stock for product: "
                                    + product.getProductName()
                                    + ". Available: "
                                    + currentStock
                                    + ", Required: "
                                    + item.getQuantity());
                }
            }

            /*
             * All products have enough stock.
             * Now perform Stock Out.
             */
            for (SalesOrderItem item : items) {

                StockTransactionRequest
                        stockRequest =
                        new StockTransactionRequest();

                stockRequest.setProductId(
                        item.getProduct().getId());

                stockRequest.setQuantity(
                        item.getQuantity());

                stockRequest.setRemarks(
                        "Stock Out for Sales Order "
                                + salesOrder
                                .getOrderNumber());

                inventoryTransactionService
                        .stockOut(stockRequest);
            }
        }

        salesOrder.setStatus(newStatus);
        salesOrder.setUpdatedAt(
                LocalDateTime.now());

        SalesOrder updated =
                salesOrderRepository.save(
                        salesOrder);

        return mapToResponse(updated);
    }

    private SalesOrderResponse mapToResponse(
            SalesOrder salesOrder) {

        List<SalesOrderItemResponse> items =
                salesOrderItemRepository
                        .findBySalesOrderId(
                                salesOrder.getId())
                        .stream()
                        .map(item ->
                                SalesOrderItemResponse
                                        .builder()
                                        .id(item.getId())
                                        .productId(
                                                item.getProduct()
                                                        .getId())
                                        .productName(
                                                item.getProduct()
                                                        .getProductName())
                                        .quantity(
                                                item.getQuantity())
                                        .unitPrice(
                                                item.getUnitPrice())
                                        .totalPrice(
                                                item.getTotalPrice())
                                        .build())
                        .toList();

        return SalesOrderResponse.builder()
                .id(salesOrder.getId())
                .orderNumber(
                        salesOrder.getOrderNumber())
                .customerId(
                        salesOrder.getCustomer()
                                .getId())
                .customerName(
                        salesOrder.getCustomer()
                                .getCustomerName())
                .status(
                        salesOrder.getStatus()
                                .name())
                .totalAmount(
                        salesOrder.getTotalAmount())
                .orderDate(
                        salesOrder.getOrderDate())
                .items(items)
                .build();
    }

    private String generateOrderNumber() {

        String date =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern(
                                                "yyyyMMdd"));

        long count =
                salesOrderRepository.count() + 1;

        return "SO-" + date + "-" + count;
    }
}