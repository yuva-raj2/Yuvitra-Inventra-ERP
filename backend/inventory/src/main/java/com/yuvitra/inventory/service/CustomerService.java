package com.yuvitra.inventory.service;

import com.yuvitra.inventory.dto.request.CustomerRequest;
import com.yuvitra.inventory.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(
            CustomerRequest request);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse getCustomerById(Long id);

    CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request);

    String deleteCustomer(Long id);

    List<CustomerResponse> searchCustomers(
            String customerName);
}