package com.yuvitra.inventory.entity;

import com.yuvitra.inventory.entity.enums.SalesOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SalesOrderStatus status =
            SalesOrderStatus.DRAFT;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private LocalDateTime orderDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}