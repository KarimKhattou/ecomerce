package de.ecom.ecomapplication.service;

import de.ecom.ecomapplication.dto.CartItemResponse;
import de.ecom.ecomapplication.dto.OrderItemDTO;
import de.ecom.ecomapplication.dto.OrderResponse;
import de.ecom.ecomapplication.model.Order;
import de.ecom.ecomapplication.model.OrderItem;
import de.ecom.ecomapplication.model.OrderStatus;
import de.ecom.ecomapplication.model.User;
import de.ecom.ecomapplication.repository.OrderRepository;
import de.ecom.ecomapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        // Validate User
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User userValid = userOpt.get();

        // Validate Cart items
        List<CartItemResponse> cartItemsResponse = cartService.getCarts(userValid.getId());
        if (cartItemsResponse.isEmpty()) {
            return Optional.empty();
        }

        // Calculate Total price
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalPriceValid = cartItemsResponse.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create Order
        Order order = new Order();
        order.setUser(userValid);
        order.setTotalPrice(totalPriceValid);
        order.setStatus(OrderStatus.CONFIRMED);
        List<OrderItem> orderItems = cartItemsResponse.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderItems().stream()
                        .map(orderItem ->
                                new OrderItemDTO(orderItem.getItemId(),
                                        orderItem.getProduct().getId(),
                                        orderItem.getQuantity(),
                                        orderItem.getPrice(),
                                        orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                        )).toList(),
                order.getCreatedAt()
        );
    }
}
