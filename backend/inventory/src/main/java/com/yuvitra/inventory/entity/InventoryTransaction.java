package com.yuvitra.inventory.entity;

import com.yuvitra.inventory.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Integer quantity;

    private Integer previousStock;

    private Integer currentStock;

    private String remarks;

    private LocalDateTime transactionDate;
}