package de.ecom.ecomapplication.controller;

import de.ecom.ecomapplication.dto.CartItemRequest;
import de.ecom.ecomapplication.dto.CartItemResponse;
import de.ecom.ecomapplication.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCarts(
            @RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCarts(Long.valueOf(userId)));
    }

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest cartRequest) {
        String response = cartService.addToCart(userId, cartRequest).getBody();
        assert response != null;
        return switch (response) {
            case "P" -> ResponseEntity.badRequest().body("Product not found !");
            case "Q" -> ResponseEntity.badRequest().body("Quantity out of range !");
            case "U" -> ResponseEntity.badRequest().body("User not found !");
            default -> ResponseEntity.ok().body("Item added successfully!");
        };
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId
    ) {
        boolean deleted = cartService.removeItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
