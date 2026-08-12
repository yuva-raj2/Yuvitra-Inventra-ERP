package com.yuvitra.inventory.controller;
import com.yuvitra.inventory.dto.response.InventoryValuationResponse;
import com.yuvitra.inventory.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/inventory-valuation")
    public List<InventoryValuationResponse>
    getInventoryValuationReport() {

        return reportService
                .getInventoryValuationReport();
    }
}