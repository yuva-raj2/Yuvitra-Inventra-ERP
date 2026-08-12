package com.yuvitra.inventory.service.impl;
import com.yuvitra.inventory.dto.request.ReceivePurchaseOrderRequest;
import com.yuvitra.inventory.dto.request.ReceivePurchaseOrderItemRequest;
import com.yuvitra.inventory.dto.request.StockTransactionRequest;
import com.yuvitra.inventory.service.InventoryTransactionService;
import com.yuvitra.inventory.dto.request.PurchaseOrderItemRequest;
import com.yuvitra.inventory.dto.request.PurchaseOrderRequest;
import com.yuvitra.inventory.dto.response.PurchaseOrderItemResponse;
import com.yuvitra.inventory.dto.response.PurchaseOrderResponse;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.entity.PurchaseOrder;
import com.yuvitra.inventory.entity.PurchaseOrderItem;
import com.yuvitra.inventory.entity.Supplier;
import com.yuvitra.inventory.entity.enums.PurchaseOrderStatus;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.repository.PurchaseOrderItemRepository;
import com.yuvitra.inventory.repository.PurchaseOrderRepository;
import com.yuvitra.inventory.repository.SupplierRepository;
import com.yuvitra.inventory.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl
        implements PurchaseOrderService {
    private final InventoryTransactionService
            inventoryTransactionService;

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final PurchaseOrderItemRepository
            purchaseOrderItemRepository;

    private final SupplierRepository supplierRepository;

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(
            PurchaseOrderRequest request) {

        Supplier supplier =
                supplierRepository.findById(
                                request.getSupplierId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier not found with id: "
                                                + request.getSupplierId()));

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new IllegalArgumentException(
                    "Purchase order must contain at least one item");
        }

        String orderNumber =
                generateOrderNumber();

        PurchaseOrder purchaseOrder =
                PurchaseOrder.builder()
                        .orderNumber(orderNumber)
                        .supplier(supplier)
                        .status(
                                PurchaseOrderStatus.DRAFT)
                        .totalAmount(BigDecimal.ZERO)
                        .orderDate(
                                LocalDateTime.now())
                        .expectedDeliveryDate(
                                request.getExpectedDeliveryDate())
                        .createdAt(
                                LocalDateTime.now())
                        .updatedAt(
                                LocalDateTime.now())
                        .build();

        PurchaseOrder savedOrder =
                purchaseOrderRepository.save(
                        purchaseOrder);

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (PurchaseOrderItemRequest itemRequest :
                request.getItems()) {

            Product product =
                    productRepository.findById(
                                    itemRequest.getProductId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Product not found with id: "
                                                    + itemRequest.getProductId()));

            BigDecimal itemTotal =
                    itemRequest.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()));

            PurchaseOrderItem item =
                    PurchaseOrderItem.builder()
                            .purchaseOrder(savedOrder)
                            .product(product)
                            .quantity(
                                    itemRequest.getQuantity())
                            .receivedQuantity(0)
                            .unitPrice(
                                    itemRequest.getUnitPrice())
                            .totalPrice(itemTotal)
                            .build();

            purchaseOrderItemRepository.save(item);

            totalAmount =
                    totalAmount.add(itemTotal);
        }

        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setUpdatedAt(
                LocalDateTime.now());

        PurchaseOrder updatedOrder =
                purchaseOrderRepository.save(
                        savedOrder);

        return mapToResponse(updatedOrder);
    }

    @Override
    public List<PurchaseOrderResponse>
    getAllPurchaseOrders() {

        return purchaseOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PurchaseOrderResponse
    getPurchaseOrderById(Long id) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Purchase order not found with id: "
                                                + id));

        return mapToResponse(purchaseOrder);
    }
    @Override
    @Transactional
    public PurchaseOrderResponse receivePurchaseOrder(
            Long id,
            ReceivePurchaseOrderRequest request) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Purchase order not found with id: "
                                                + id));

        if (purchaseOrder.getStatus()
                == PurchaseOrderStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Cancelled purchase order cannot be received");
        }

        if (purchaseOrder.getStatus()
                == PurchaseOrderStatus.RECEIVED) {

            throw new IllegalStateException(
                    "Purchase order is already fully received");
        }

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one item must be received");
        }

        for (ReceivePurchaseOrderItemRequest
                receiveRequest : request.getItems()) {

            PurchaseOrderItem item =
                    purchaseOrderItemRepository
                            .findById(
                                    receiveRequest
                                            .getPurchaseOrderItemId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Purchase order item not found with id: "
                                                    + receiveRequest
                                                    .getPurchaseOrderItemId()));

            if (!item.getPurchaseOrder()
                    .getId()
                    .equals(purchaseOrder.getId())) {

                throw new IllegalArgumentException(
                        "Purchase order item does not belong to this purchase order");
            }

            Integer receiveQuantity =
                    receiveRequest.getQuantity();

            if (receiveQuantity == null ||
                    receiveQuantity <= 0) {

                throw new IllegalArgumentException(
                        "Receive quantity must be greater than zero");
            }

            Integer alreadyReceived =
                    item.getReceivedQuantity() == null
                            ? 0
                            : item.getReceivedQuantity();

            Integer remainingQuantity =
                    item.getQuantity()
                            - alreadyReceived;

            if (receiveQuantity >
                    remainingQuantity) {

                throw new IllegalArgumentException(
                        "Cannot receive more than remaining quantity for product: "
                                + item.getProduct()
                                .getProductName());
            }

            StockTransactionRequest
                    stockTransactionRequest =
                    new StockTransactionRequest();

            stockTransactionRequest.setProductId(
                    item.getProduct().getId());

            stockTransactionRequest.setQuantity(
                    receiveQuantity);

            stockTransactionRequest.setRemarks(
                    "Received against Purchase Order "
                            + purchaseOrder.getOrderNumber());

            inventoryTransactionService.stockIn(
                    stockTransactionRequest);

            item.setReceivedQuantity(
                    alreadyReceived
                            + receiveQuantity);

            purchaseOrderItemRepository.save(item);
        }

        boolean fullyReceived =
                purchaseOrderItemRepository
                        .findByPurchaseOrderId(
                                purchaseOrder.getId())
                        .stream()
                        .allMatch(item ->
                                item.getReceivedQuantity() != null
                                        &&
                                        item.getReceivedQuantity()
                                                >= item.getQuantity());

        if (fullyReceived) {

            purchaseOrder.setStatus(
                    PurchaseOrderStatus.RECEIVED);

        } else {

            purchaseOrder.setStatus(
                    PurchaseOrderStatus.PARTIALLY_RECEIVED);
        }

        purchaseOrder.setUpdatedAt(
                LocalDateTime.now());

        PurchaseOrder updated =
                purchaseOrderRepository.save(
                        purchaseOrder);

        return mapToResponse(updated);
    }
    @Override
    public PurchaseOrderResponse updateStatus(
            Long id,
            String status) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Purchase order not found with id: "
                                                + id));

        PurchaseOrderStatus newStatus;

        try {
            newStatus =
                    PurchaseOrderStatus.valueOf(
                            status.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid purchase order status: "
                            + status);
        }

        purchaseOrder.setStatus(newStatus);
        purchaseOrder.setUpdatedAt(
                LocalDateTime.now());

        PurchaseOrder updated =
                purchaseOrderRepository.save(
                        purchaseOrder);

        return mapToResponse(updated);
    }

    private PurchaseOrderResponse mapToResponse(
            PurchaseOrder purchaseOrder) {

        List<PurchaseOrderItemResponse> items =
                purchaseOrderItemRepository
                        .findByPurchaseOrderId(
                                purchaseOrder.getId())
                        .stream()
                        .map(item ->
                                PurchaseOrderItemResponse
                                        .builder()
                                        .id(item.getId())
                                        .productId(
                                                item.getProduct().getId())
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

        return PurchaseOrderResponse.builder()
                .id(purchaseOrder.getId())
                .orderNumber(
                        purchaseOrder.getOrderNumber())
                .supplierId(
                        purchaseOrder.getSupplier()
                                .getId())
                .supplierName(
                        purchaseOrder.getSupplier()
                                .getSupplierName())
                .status(
                        purchaseOrder.getStatus()
                                .name())
                .totalAmount(
                        purchaseOrder.getTotalAmount())
                .orderDate(
                        purchaseOrder.getOrderDate())
                .expectedDeliveryDate(
                        purchaseOrder
                                .getExpectedDeliveryDate())
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
                purchaseOrderRepository.count() + 1;

        return "PO-" + date + "-" + count;

    }
}