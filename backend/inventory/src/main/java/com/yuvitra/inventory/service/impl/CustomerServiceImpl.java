package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.request.CustomerRequest;
import com.yuvitra.inventory.dto.response.CustomerResponse;
import com.yuvitra.inventory.entity.Customer;
import com.yuvitra.inventory.exception.DuplicateResourceException;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.repository.CustomerRepository;
import com.yuvitra.inventory.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl
        implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(
            CustomerRequest request) {

        if (request.getCustomerName() == null ||
                request.getCustomerName().isBlank()) {

            throw new IllegalArgumentException(
                    "Customer name is required");
        }

        if (request.getEmail() != null &&
                !request.getEmail().isBlank() &&
                customerRepository.existsByEmail(
                        request.getEmail())) {

            throw new DuplicateResourceException(
                    "Customer email already exists");
        }

        Customer customer = Customer.builder()
                .customerName(
                        request.getCustomerName())
                .email(
                        request.getEmail())
                .phone(
                        request.getPhone())
                .address(
                        request.getAddress())
                .city(
                        request.getCity())
                .active(true)
                .createdAt(
                        LocalDateTime.now())
                .updatedAt(
                        LocalDateTime.now())
                .build();

        Customer saved =
                customerRepository.save(customer);

        return mapToResponse(saved);
    }

    @Override
    public List<CustomerResponse>
    getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CustomerResponse
    getCustomerById(Long id) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id));

        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id));

        if (request.getEmail() != null &&
                !request.getEmail().isBlank() &&
                !request.getEmail()
                        .equalsIgnoreCase(
                                customer.getEmail()) &&
                customerRepository.existsByEmail(
                        request.getEmail())) {

            throw new DuplicateResourceException(
                    "Customer email already exists");
        }

        customer.setCustomerName(
                request.getCustomerName());

        customer.setEmail(
                request.getEmail());

        customer.setPhone(
                request.getPhone());

        customer.setAddress(
                request.getAddress());

        customer.setCity(
                request.getCity());

        customer.setUpdatedAt(
                LocalDateTime.now());

        Customer updated =
                customerRepository.save(customer);

        return mapToResponse(updated);
    }

    @Override
    public String deleteCustomer(Long id) {

        Customer customer =
                customerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + id));

        customerRepository.delete(customer);

        return "Customer deleted successfully";
    }

    @Override
    public List<CustomerResponse>
    searchCustomers(String customerName) {

        return customerRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CustomerResponse mapToResponse(
            Customer customer) {

        return CustomerResponse.builder()
                .id(customer.getId())
                .customerName(
                        customer.getCustomerName())
                .email(
                        customer.getEmail())
                .phone(
                        customer.getPhone())
                .address(
                        customer.getAddress())
                .city(
                        customer.getCity())
                .active(
                        customer.getActive())
                .createdAt(
                        customer.getCreatedAt())
                .updatedAt(
                        customer.getUpdatedAt())
                .build();
    }
}