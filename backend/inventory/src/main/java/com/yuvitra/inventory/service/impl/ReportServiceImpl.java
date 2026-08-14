package com.yuvitra.inventory.service.impl;
import com.yuvitra.inventory.dto.response.*;
import com.yuvitra.inventory.entity.Product;
import com.yuvitra.inventory.entity.SalesOrder;
import com.yuvitra.inventory.entity.enums.SalesOrderStatus;
import com.yuvitra.inventory.repository.ProductRepository;
import com.yuvitra.inventory.service.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.yuvitra.inventory.entity.PurchaseOrder;
import org.springframework.stereotype.Service;
import com.yuvitra.inventory.repository.PurchaseOrderRepository;
import java.math.BigDecimal;
import com.yuvitra.inventory.repository.SalesOrderRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SalesOrderRepository salesOrderRepository;
    private final ProductRepository productRepository;
private final PurchaseOrderRepository purchaseOrderRepository;

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
    @Override
    public List<PurchaseReportResponse>
    getPurchaseReport() {

        return purchaseOrderRepository.findAll()
                .stream()
                .map(po ->
                        PurchaseReportResponse.builder()
                                .purchaseOrderId(po.getId())
                                .purchaseOrderNumber(
                                        po.getOrderNumber())
                                .supplierName(
                                        po.getSupplier()
                                                .getSupplierName())
                                .status(
                                        po.getStatus().name())
                                .totalAmount(
                                        po.getTotalAmount())
                                .totalItems(
                                        po.getItems() == null
                                                ? 0
                                                : po.getItems().size())
                                .build())
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
    @Override
    public List<SalesReportResponse> getSalesReport() {

        return salesOrderRepository.findAll()
                .stream()
                .map(so -> SalesReportResponse.builder()
                        .salesOrderNumber(so.getOrderNumber())
                        .customerName(
                                so.getCustomer() != null
                                        ? so.getCustomer().getCustomerName()
                                        : "N/A"
                        )
                        .status(so.getStatus().name())
                        .totalAmount(so.getTotalAmount())
                        .build())
                .toList();
    }
    @Override
    public List<SupplierPerformanceResponse>
    getSupplierPerformanceReport() {

        return purchaseOrderRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        po -> po.getSupplier().getSupplierName()
                ))
                .entrySet()
                .stream()
                .map(entry -> SupplierPerformanceResponse.builder()
                        .supplierName(entry.getKey())
                        .totalPurchaseOrders(
                                (long) entry.getValue().size()
                        )
                        .totalPurchaseAmount(
                                entry.getValue()
                                        .stream()
                                        .map(PurchaseOrder::getTotalAmount)
                                        .reduce(
                                                BigDecimal.ZERO,
                                                BigDecimal::add
                                        )
                        )
                        .build())
                .toList();
    }
    @Override
    public InventorySummaryResponse
    getInventorySummaryReport() {

        List<Product> products =
                productRepository.findAll();

        long totalProducts =
                products.size();

        int totalStock =
                products.stream()
                        .map(Product::getQuantity)
                        .filter(Objects::nonNull)
                        .reduce(0, Integer::sum);

        BigDecimal inventoryValue =
                products.stream()
                        .map(product -> {

                            Integer qty =
                                    product.getQuantity() == null
                                            ? 0
                                            : product.getQuantity();

                            BigDecimal price =
                                    product.getUnitPrice() == null
                                            ? BigDecimal.ZERO
                                            : product.getUnitPrice();

                            return price.multiply(
                                    BigDecimal.valueOf(qty));
                        })
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        long lowStockProducts =
                products.stream()
                        .filter(product -> {

                            Integer qty =
                                    product.getQuantity();

                            Integer minStock =
                                    product.getMinimumStock();

                            if (qty == null || minStock == null) {
                                return false;
                            }

                            return qty <= minStock;
                        })
                        .count();

        return InventorySummaryResponse.builder()
                .totalProducts(totalProducts)
                .totalStock(totalStock)
                .inventoryValue(inventoryValue)
                .lowStockProducts(lowStockProducts)
                .build();
    }
    @Override
    public SalesAnalyticsResponse
    getSalesAnalyticsReport() {

        List<SalesOrder> salesOrders =
                salesOrderRepository.findAll();

        long totalOrders =
                salesOrders.size();

        long completedOrders =
                salesOrders.stream()
                        .filter(order ->
                                order.getStatus()
                                        == SalesOrderStatus.DELIVERED)
                        .count();

        long pendingOrders =
                salesOrders.stream()
                        .filter(order ->
                                order.getStatus()
                                        != SalesOrderStatus.DELIVERED
                                        &&
                                        order.getStatus()
                                                != SalesOrderStatus.CANCELLED)
                        .count();

        BigDecimal totalRevenue =
                salesOrders.stream()
                        .map(order ->
                                order.getTotalAmount() == null
                                        ? BigDecimal.ZERO
                                        : order.getTotalAmount())
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        return SalesAnalyticsResponse.builder()
                .totalSalesOrders(totalOrders)
                .completedSalesOrders(completedOrders)
                .pendingSalesOrders(pendingOrders)
                .totalRevenue(totalRevenue)
                .build();
    }
}