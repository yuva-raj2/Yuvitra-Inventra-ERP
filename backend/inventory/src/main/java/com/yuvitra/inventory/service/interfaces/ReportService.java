package com.yuvitra.inventory.service.interfaces;

import com.yuvitra.inventory.dto.response.InventoryValuationResponse;
import com.yuvitra.inventory.dto.response.LowStockReportResponse;

import java.util.List;

public interface ReportService {

    List<InventoryValuationResponse>
    getInventoryValuationReport();

    List<LowStockReportResponse>
    getLowStockReport();
}