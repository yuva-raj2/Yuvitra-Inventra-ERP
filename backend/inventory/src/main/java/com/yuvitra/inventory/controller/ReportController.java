package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.dto.response.InventoryValuationResponse;
import com.yuvitra.inventory.dto.response.LowStockReportResponse;
import com.yuvitra.inventory.dto.response.PurchaseReportResponse;
import com.yuvitra.inventory.dto.response.SalesReportResponse;
import com.yuvitra.inventory.service.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuvitra.inventory.dto.response.SupplierPerformanceResponse;
import java.util.List;
import com.yuvitra.inventory.dto.response.InventorySummaryResponse;
import com.yuvitra.inventory.dto.response.SalesAnalyticsResponse;
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    // =========================================================
    // INVENTORY VALUATION
    // =========================================================

    @GetMapping("/inventory-valuation")
    public List<InventoryValuationResponse>
    getInventoryValuationReport() {

        return reportService
                .getInventoryValuationReport();
    }

    @GetMapping("/purchases")
    public List<PurchaseReportResponse>
    getPurchaseReport() {

        return reportService
                .getPurchaseReport();
    }
    // =========================================================
    // LOW STOCK REPORT
    // =========================================================

    @GetMapping("/low-stock")
    public List<LowStockReportResponse>
    getLowStockReport() {

        return reportService
                .getLowStockReport();
    }
    @GetMapping("/sales")
    public List<SalesReportResponse> getSalesReport() {

        return reportService.getSalesReport();
    }
    @GetMapping("/suppliers")
    public List<SupplierPerformanceResponse>
    getSupplierPerformanceReport() {

        return reportService
                .getSupplierPerformanceReport();
    }
    @GetMapping("/inventory-summary")
    public InventorySummaryResponse
    getInventorySummaryReport() {

        return reportService
                .getInventorySummaryReport();
    }
    @GetMapping("/sales-analytics")
    public SalesAnalyticsResponse
    getSalesAnalyticsReport() {

        return reportService
                .getSalesAnalyticsReport();
    }
}