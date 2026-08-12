package com.yuvitra.inventory.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supplierName;

    private String contactPerson;

    private String email;

    private String phone;

    private String address;

    private String city;

    private Boolean active;

    private LocalDateTime createdAt;
}