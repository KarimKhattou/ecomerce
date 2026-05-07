package de.ecom.ecomapplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "product_id",  nullable = false)
    private Product product;

    private BigDecimal quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id",  nullable = false)
    private Order order;
}
