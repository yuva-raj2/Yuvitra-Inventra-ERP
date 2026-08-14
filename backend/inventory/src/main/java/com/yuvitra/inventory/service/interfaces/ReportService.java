package com.yuvitra.inventory.service.interfaces;

import com.yuvitra.inventory.dto.response.*;

import java.util.List;
import com.yuvitra.inventory.dto.response.InventorySummaryResponse;
import com.yuvitra.inventory.dto.response.SalesAnalyticsResponse;
public interface ReportService {

    List<InventoryValuationResponse>
    getInventoryValuationReport();

    List<LowStockReportResponse>
    getLowStockReport();
    List<SupplierPerformanceResponse>
    getSupplierPerformanceReport();
    List<PurchaseReportResponse>
    getPurchaseReport();
    List<SalesReportResponse> getSalesReport();
    InventorySummaryResponse getInventorySummaryReport();
    SalesAnalyticsResponse
    getSalesAnalyticsReport();
}