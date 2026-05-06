package de.ecom.ecomapplication.controller;

import de.ecom.ecomapplication.dto.CartItemRequest;
import de.ecom.ecomapplication.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest cartRequest) {
        if (!cartService.addToCart(userId, cartRequest))
            return ResponseEntity.badRequest().body("User or Product not found or out of Stock");
        else
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
