package com.yuvitra.inventory.controller;

import com.yuvitra.inventory.dto.request.CustomerRequest;
import com.yuvitra.inventory.dto.response.CustomerResponse;
import com.yuvitra.inventory.service.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public CustomerResponse createCustomer(
            @RequestBody CustomerRequest request) {

        return customerService.createCustomer(request);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers() {

        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(
            @PathVariable Long id) {

        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequest request) {

        return customerService.updateCustomer(
                id,
                request);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomer(
            @PathVariable Long id) {

        return customerService.deleteCustomer(id);
    }

    @GetMapping("/search")
    public List<CustomerResponse> searchCustomers(
            @RequestParam String customerName) {

        return customerService.searchCustomers(
                customerName);
    }
}