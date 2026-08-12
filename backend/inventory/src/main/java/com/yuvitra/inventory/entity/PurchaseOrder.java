package com.yuvitra.inventory.entity;

import com.yuvitra.inventory.entity.enums.PurchaseOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;

    private BigDecimal totalAmount;

    private LocalDateTime orderDate;

    private LocalDateTime expectedDeliveryDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}