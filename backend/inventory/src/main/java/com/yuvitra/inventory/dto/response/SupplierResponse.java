package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SupplierResponse {

    private Long id;

    private String supplierName;

    private String contactPerson;

    private String email;

    private String phone;

    private String city;

    private Boolean active;
}
