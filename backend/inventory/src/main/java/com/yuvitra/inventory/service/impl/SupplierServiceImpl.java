package com.yuvitra.inventory.service.impl;

import com.yuvitra.inventory.dto.request.SupplierRequest;
import com.yuvitra.inventory.dto.response.SupplierResponse;
import com.yuvitra.inventory.entity.Supplier;
import com.yuvitra.inventory.exception.ResourceNotFoundException;
import com.yuvitra.inventory.repository.SupplierRepository;
import com.yuvitra.inventory.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    public SupplierResponse createSupplier(
            SupplierRequest request) {

        Supplier supplier = Supplier.builder()
                .supplierName(request.getSupplierName())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        Supplier saved =
                supplierRepository.save(supplier);

        return mapToResponse(saved);
    }

    @Override
    public List<SupplierResponse> getAllSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SupplierResponse getSupplierById(
            Long id) {

        Supplier supplier =
                supplierRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier not found with id: "
                                                + id));

        return mapToResponse(supplier);
    }

    @Override
    public SupplierResponse updateSupplier(
            Long id,
            SupplierRequest request) {

        Supplier supplier =
                supplierRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier not found with id: "
                                                + id));

        supplier.setSupplierName(
                request.getSupplierName());

        supplier.setContactPerson(
                request.getContactPerson());

        supplier.setEmail(
                request.getEmail());

        supplier.setPhone(
                request.getPhone());

        supplier.setAddress(
                request.getAddress());

        supplier.setCity(
                request.getCity());

        Supplier updated =
                supplierRepository.save(supplier);

        return mapToResponse(updated);
    }

    @Override
    public String deleteSupplier(Long id) {

        Supplier supplier =
                supplierRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Supplier not found with id: "
                                                + id));

        supplierRepository.delete(supplier);

        return "Supplier Deleted Successfully";
    }

    private SupplierResponse mapToResponse(
            Supplier supplier) {

        return SupplierResponse.builder()
                .id(supplier.getId())
                .supplierName(
                        supplier.getSupplierName())
                .contactPerson(
                        supplier.getContactPerson())
                .email(
                        supplier.getEmail())
                .phone(
                        supplier.getPhone())
                .city(
                        supplier.getCity())
                .active(
                        supplier.getActive())
                .build();
    }
}