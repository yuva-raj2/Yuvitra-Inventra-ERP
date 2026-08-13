package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.request.SalesOrderItemRequest;
import com.yuvitra.inventory.dto.request.SalesOrderRequest;
import com.yuvitra.inventory.dto.request.StockTransactionRequest;
import com.yuvitra.inventory.dto.response.SalesOrderItemResponse;
import com.yuvitra.inventory.dto.response.SalesOrderResponse;
import com.yuvitra.inventory.entity.Customer;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.entity.SalesOrder;
import com.yuvitra.inventory.entity.SalesOrderItem;
import com.yuvitra.inventory.entity.enums.SalesOrderStatus;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.repository.CustomerRepository;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.repository.SalesOrderItemRepository;
import com.yuvitra.inventory.repository.SalesOrderRepository;
import com.yuvitra.inventory.service.interfaces.InventoryTransactionService;
import com.yuvitra.inventory.service.interfaces.SalesOrderService;
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


    // =========================================================
    // CREATE SALES ORDER
    // =========================================================

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

        savedOrder.setTotalAmount(
                totalAmount);

        savedOrder.setUpdatedAt(
                LocalDateTime.now());

        SalesOrder updated =
                salesOrderRepository.save(
                        savedOrder);

        return mapToResponse(updated);
    }


    // =========================================================
    // UPDATE SALES ORDER STATUS
    // =========================================================

    @Override
    @Transactional
    public SalesOrderResponse updateStatus(
            Long id,
            SalesOrderStatus newStatus) {

        SalesOrder salesOrder =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sales order not found with id: "
                                                + id));

        /*
         * Prevent duplicate confirmation.
         */
        if (newStatus ==
                SalesOrderStatus.CONFIRMED
                &&
                salesOrder.getStatus() ==
                        SalesOrderStatus.CONFIRMED) {

            throw new IllegalArgumentException(
                    "Order is already confirmed");
        }


        /*
         * Stock validation and Stock Out
         *
         * Stock is reduced only when
         * the order becomes CONFIRMED.
         */
        if (newStatus ==
                SalesOrderStatus.CONFIRMED
                &&
                salesOrder.getStatus() !=
                        SalesOrderStatus.CONFIRMED) {

            List<SalesOrderItem> items =
                    salesOrderItemRepository
                            .findBySalesOrderId(id);


            // -------------------------------------------------
            // STEP 1: Validate ALL stock first
            // -------------------------------------------------

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


            // -------------------------------------------------
            // STEP 2: Perform Stock Out
            // -------------------------------------------------

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


        // -----------------------------------------------------
        // Update Sales Order Status
        // -----------------------------------------------------

        salesOrder.setStatus(
                newStatus);

        salesOrder.setUpdatedAt(
                LocalDateTime.now());

        SalesOrder updated =
                salesOrderRepository.save(
                        salesOrder);

        return mapToResponse(updated);
    }


    // =========================================================
    // GET ALL SALES ORDERS
    // =========================================================

    @Override
    public List<SalesOrderResponse>
    getAllSalesOrders() {

        return salesOrderRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET SALES ORDER BY ID
    // =========================================================

    @Override
    public SalesOrderResponse
    getSalesOrderById(Long id) {

        SalesOrder salesOrder =
                salesOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sales order not found with id: "
                                                + id));

        return mapToResponse(
                salesOrder);
    }


    // =========================================================
    // MAP ENTITY TO RESPONSE
    // =========================================================

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
                                        .id(
                                                item.getId())
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
                .id(
                        salesOrder.getId())
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


    // =========================================================
    // GENERATE ORDER NUMBER
    // =========================================================

    private String generateOrderNumber() {

        String date =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern(
                                                "yyyyMMdd"));

        long count =
                salesOrderRepository.count()
                        + 1;

        return "SO-"
                + date
                + "-"
                + count;
    }
}