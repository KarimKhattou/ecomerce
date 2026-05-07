package de.ecom.ecomapplication.service;

import de.ecom.ecomapplication.dto.CartItemRequest;
import de.ecom.ecomapplication.dto.CartItemResponse;
import de.ecom.ecomapplication.dto.ProductResponse;
import de.ecom.ecomapplication.model.CardItem;
import de.ecom.ecomapplication.model.Product;
import de.ecom.ecomapplication.model.User;
import de.ecom.ecomapplication.repository.CardItemRepository;
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
    private final CardItemRepository cardItemRepository;

    public List<CartItemResponse> getAllUser() {
        return cardItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CartItemResponse mapToResponse(CardItem cardItem) {
        CartItemResponse cardResponse = new CartItemResponse();
        cardResponse.setUser(cardItem.getUser());
        cardResponse.setProduct(cardItem.getProduct());
        cardResponse.setQuantity(cardItem.getQuantity());
        cardResponse.setPrice(cardItem.getPrice());
        cardResponse.setCreatedAt(cardItem.getCreatedAt());
        cardResponse.setUpdatedAt(cardItem.getUpdatedAt());

        return cardResponse;
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

        CardItem existingCardItem = cardItemRepository.findByUserAndProduct(user, product);
        if (existingCardItem != null) {
            // update the quantity
            existingCardItem.setQuantity(existingCardItem.getQuantity() + cartRequest.getQuantity());
            existingCardItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
            cardItemRepository.save(existingCardItem);
        } else {
            // create a new card item
            CardItem cardItem = new CardItem();
            cardItem.setUser(user);
            cardItem.setProduct(product);
            cardItem.setQuantity(product.getQuantity());
            cardItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
            cardItemRepository.save(cardItem);
        }

        return ResponseEntity.ok().body("Item added successfully!");
    }

    public boolean removeItemFromCart(String userId, String productId) {
        Optional<Product> productOptional = productRepository.findById(Long.valueOf(productId));
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

        if (productOptional.isPresent() && userOptional.isPresent()) {
            cardItemRepository.deleteByUserAndProduct(userOptional.get(), productOptional.get());
            return true;
        }
        return false;
    }
}
