package de.ecom.ecomapplication.dto;

import de.ecom.ecomapplication.model.Product;
import de.ecom.ecomapplication.model.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItemResponse {

    private User user;
    private Product product;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
