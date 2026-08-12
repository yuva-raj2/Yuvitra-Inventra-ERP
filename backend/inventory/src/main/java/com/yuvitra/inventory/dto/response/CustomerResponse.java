package com.yuvitra.inventory.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {

    private Long id;

    private String customerName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}