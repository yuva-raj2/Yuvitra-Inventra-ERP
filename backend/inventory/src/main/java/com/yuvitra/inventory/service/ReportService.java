package com.yuvitra.inventory.service;
import com.yuvitra.inventory.dto.response.InventoryValuationResponse;
import java.util.List;
public interface ReportService {

    List<InventoryValuationResponse>
    getInventoryValuationReport();
}