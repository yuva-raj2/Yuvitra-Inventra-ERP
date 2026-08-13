package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.response.InventoryValuationResponse;
import com.yuvitra.inventory.dto.response.LowStockReportResponse;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.service.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ProductRepository productRepository;


    // =========================================================
    // INVENTORY VALUATION REPORT
    // =========================================================

    @Override
    public List<InventoryValuationResponse>
    getInventoryValuationReport() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToInventoryValuation)
                .toList();
    }


    private InventoryValuationResponse
    mapToInventoryValuation(Product product) {

        Integer quantity =
                product.getQuantity() == null
                        ? 0
                        : product.getQuantity();

        BigDecimal unitPrice =
                product.getUnitPrice() == null
                        ? BigDecimal.ZERO
                        : product.getUnitPrice();

        BigDecimal inventoryValue =
                unitPrice.multiply(
                        BigDecimal.valueOf(quantity));

        return InventoryValuationResponse.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .inventoryValue(inventoryValue)
                .build();
    }


    // =========================================================
    // LOW STOCK REPORT
    // =========================================================

    @Override
    public List<LowStockReportResponse>
    getLowStockReport() {

        return productRepository.findAll()
                .stream()
                .filter(this::isLowStock)
                .map(this::mapToLowStockReport)
                .toList();
    }


    private boolean isLowStock(Product product) {

        if (product.getQuantity() == null) {
            return true;
        }

        if (product.getMinimumStock() == null) {
            return false;
        }

        return product.getQuantity()
                <= product.getMinimumStock();
    }


    private LowStockReportResponse
    mapToLowStockReport(Product product) {

        Integer quantity =
                product.getQuantity() == null
                        ? 0
                        : product.getQuantity();

        Integer minimumStock =
                product.getMinimumStock() == null
                        ? 0
                        : product.getMinimumStock();

        Integer shortage =
                Math.max(
                        minimumStock - quantity,
                        0);

        BigDecimal unitPrice =
                product.getUnitPrice() == null
                        ? BigDecimal.ZERO
                        : product.getUnitPrice();

        BigDecimal reorderValue =
                unitPrice.multiply(
                        BigDecimal.valueOf(
                                shortage));

        return LowStockReportResponse.builder()
                .productId(product.getId())
                .productName(
                        product.getProductName())
                .sku(
                        product.getSku())
                .category(
                        product.getCategory())
                .quantity(quantity)
                .minimumStock(minimumStock)
                .shortage(shortage)
                .unitPrice(unitPrice)
                .reorderValue(reorderValue)
                .build();
    }
}