package de.ecom.ecomapplication.dto;

import de.ecom.ecomapplication.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private List<OrderItemDTO> orderItemsDto;
    private LocalDateTime createdAt;
}
