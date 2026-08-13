package com.yuvitra.inventory.service.interfaces;
import com.yuvitra.inventory.dto.request.SupplierRequest;
import com.yuvitra.inventory.dto.response.SupplierResponse;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(
            SupplierRequest request);

    List<SupplierResponse> getAllSuppliers();

    SupplierResponse getSupplierById(
            Long id);

    SupplierResponse updateSupplier(
            Long id,
            SupplierRequest request);

    String deleteSupplier(
            Long id);
}
