package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.entity.InventoryTransaction;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.entity.enums.TransactionType;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.repository.InventoryTransactionRepository;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.yuvitra.inventory.dto.response.InventoryTransactionResponse;
import com.yuvitra.inventory.dto.request.StockTransactionRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl
        implements InventoryTransactionService {

    private final ProductRepository productRepository;

    private final InventoryTransactionRepository
            transactionRepository;
    @Override
    public InventoryTransactionResponse stockIn(
            StockTransactionRequest request) {

        Product product =
                productRepository.findById(
                                request.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product Not Found"));

        Integer previousStock =
                product.getQuantity();

        product.setQuantity(
                product.getQuantity()
                        + request.getQuantity());

        productRepository.save(product);

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .product(product)
                        .transactionType(
                                TransactionType.STOCK_IN)
                        .quantity(
                                request.getQuantity())
                        .previousStock(
                                previousStock)
                        .currentStock(
                                product.getQuantity())
                        .remarks(
                                request.getRemarks())
                        .transactionDate(
                                LocalDateTime.now())
                        .build();

        InventoryTransaction saved =
                transactionRepository.save(
                        transaction);

        return InventoryTransactionResponse
                .builder()
                .id(saved.getId())
                .productName(
                        product.getProductName())
                .transactionType(
                        saved.getTransactionType()
                                .name())
                .quantity(saved.getQuantity())
                .previousStock(
                        saved.getPreviousStock())
                .currentStock(
                        saved.getCurrentStock())
                .remarks(saved.getRemarks())
                .build();
    }
    @Override
    public InventoryTransactionResponse stockOut(
            StockTransactionRequest request) {

        Product product =
                productRepository.findById(
                                request.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product Not Found"));

        if(product.getQuantity()
                < request.getQuantity()) {

           /* throw new RuntimeException(
                    "Insufficient Stock");*/
        }

        Integer previousStock =
                product.getQuantity();

        product.setQuantity(
                product.getQuantity()
                        - request.getQuantity());

        productRepository.save(product);

        InventoryTransaction transaction =
                InventoryTransaction.builder()
                        .product(product)
                        .transactionType(
                                TransactionType.STOCK_OUT)
                        .quantity(
                                request.getQuantity())
                        .previousStock(
                                previousStock)
                        .currentStock(
                                product.getQuantity())
                        .remarks(
                                request.getRemarks())
                        .transactionDate(
                                LocalDateTime.now())
                        .build();

        InventoryTransaction saved =
                transactionRepository.save(
                        transaction);

        return InventoryTransactionResponse
                .builder()
                .id(saved.getId())
                .productName(
                        product.getProductName())
                .transactionType(
                        saved.getTransactionType()
                                .name())
                .quantity(saved.getQuantity())
                .previousStock(
                        saved.getPreviousStock())
                .currentStock(
                        saved.getCurrentStock())
                .remarks(saved.getRemarks())
                .build();
    }
    @Override
    public List<InventoryTransactionResponse>
    getTransactionHistory(
            Long productId) {

        return transactionRepository
                .findByProductIdOrderByTransactionDateDesc(
                        productId)
                .stream()
                .map(transaction ->
                        InventoryTransactionResponse
                                .builder()
                                .id(transaction.getId())
                                .productName(
                                        transaction
                                                .getProduct()
                                                .getProductName())
                                .transactionType(
                                        transaction
                                                .getTransactionType()
                                                .name())
                                .quantity(
                                        transaction
                                                .getQuantity())
                                .previousStock(
                                        transaction
                                                .getPreviousStock())
                                .currentStock(
                                        transaction
                                                .getCurrentStock())
                                .remarks(
                                        transaction
                                                .getRemarks())
                                .build())
                .toList();
    }
}