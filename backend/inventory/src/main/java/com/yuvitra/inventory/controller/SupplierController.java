package com.yuvitra.inventory.controller;
import com.yuvitra.inventory.dto.request.SupplierRequest;
import com.yuvitra.inventory.dto.response.SupplierResponse;
import com.yuvitra.inventory.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public SupplierResponse createSupplier(
            @RequestBody SupplierRequest request) {

        return supplierService.createSupplier(
                request);
    }

    @GetMapping
    public List<SupplierResponse> getAllSuppliers() {

        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public SupplierResponse getSupplierById(
            @PathVariable Long id) {

        return supplierService.getSupplierById(id);
    }

    @PutMapping("/{id}")
    public SupplierResponse updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierRequest request) {

        return supplierService.updateSupplier(
                id,
                request);
    }

    @DeleteMapping("/{id}")
    public String deleteSupplier(
            @PathVariable Long id) {

        return supplierService.deleteSupplier(id);
    }
}
