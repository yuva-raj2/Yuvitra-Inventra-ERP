package com.yuvitra.inventory.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequest {
    private String supplierName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String city;
}