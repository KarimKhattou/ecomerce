package de.ecom.ecomapplication.service;

import de.ecom.ecomapplication.dto.CartItemRequest;
import de.ecom.ecomapplication.dto.CartItemResponse;
import de.ecom.ecomapplication.model.CartItem;
import de.ecom.ecomapplication.model.Product;
import de.ecom.ecomapplication.model.User;
import de.ecom.ecomapplication.repository.CartItemRepository;
import de.ecom.ecomapplication.repository.ProductRepository;
import de.ecom.ecomapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public List<CartItemResponse> getCarts(Long userId) {
        List<CartItem> card = userRepository.findById(userId)
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
        return mapToResponse(card);
    }

    private List<CartItemResponse> mapToResponse(List<CartItem> card) {
        return card.stream()
                .map(cartItem -> {
                    CartItemResponse response = new CartItemResponse();
                    response.setUser(cartItem.getUser());
                    response.setProduct(cartItem.getProduct());
                    response.setQuantity(cartItem.getQuantity());
                    response.setPrice(cartItem.getPrice());
                    response.setCreatedAt(cartItem.getCreatedAt());
                    response.setUpdatedAt(cartItem.getUpdatedAt());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> addToCart(String userId, CartItemRequest cartRequest) {
        // fetch the product
        Optional<Product> productOpt = productRepository.findById(cartRequest.getProductId());
        if (productOpt.isEmpty())
            return ResponseEntity.badRequest().body("P");
        Product product = productOpt.get();

        // check the quantity
        if (product.getQuantity() < cartRequest.getQuantity())
            return ResponseEntity.badRequest().body("Q");;

        // fetch the user
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty())
            return ResponseEntity.badRequest().body("U");;
        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (existingCartItem != null) {
            // update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // create a new card item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(product.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
            cartItemRepository.save(cartItem);
        }

        return ResponseEntity.ok().body("Item added successfully!");
    }

    public boolean removeItemFromCart(String userId, String productId) {
        Optional<Product> productOptional = productRepository.findById(Long.valueOf(productId));
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        if (productOptional.isPresent() && userOptional.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOptional.get(), productOptional.get());
            return true;
        }
        return false;
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId))
                .ifPresent(cartItemRepository::deleteByUser);
    }
}
