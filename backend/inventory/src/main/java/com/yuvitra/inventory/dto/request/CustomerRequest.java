package com.yuvitra.inventory.dto.request;

import lombok.Data;

@Data
public class CustomerRequest {

    private String customerName;

    private String email;

    private String phone;

    private String address;

    private String city;
}