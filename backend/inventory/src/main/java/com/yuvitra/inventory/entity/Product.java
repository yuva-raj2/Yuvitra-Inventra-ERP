package com.yuvitra.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    @Column(unique = true, nullable = false)
    private String sku;

    private String category;

    private String brand;

    private Integer quantity;

    private Integer minimumStock;

    private BigDecimal unitPrice;

    private String description;

    private Boolean active;

    private LocalDateTime createdAt;

    private String location;
    private String unit;
    private String supplierName;
    private LocalDateTime updatedAt;

}