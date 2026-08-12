package com.yuvitra.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sales_order_id",
            nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne
    @JoinColumn(name = "product_id",
            nullable = false)
    private Product product;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}