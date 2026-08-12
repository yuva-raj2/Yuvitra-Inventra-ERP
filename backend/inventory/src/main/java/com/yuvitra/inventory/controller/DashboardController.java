package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.dto.response.DashboardResponse;
import com.yuvitra.inventory.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/api/dashboard")
    public DashboardResponse dashboard() {

        return dashboardService.getDashboard();
    }
}