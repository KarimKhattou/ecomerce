package de.ecom.ecomapplication.repository;

import de.ecom.ecomapplication.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
