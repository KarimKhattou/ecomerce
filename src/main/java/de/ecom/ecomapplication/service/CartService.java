package de.ecom.ecomapplication.service;

import de.ecom.ecomapplication.dto.CartItemRequest;
import de.ecom.ecomapplication.model.CardItem;
import de.ecom.ecomapplication.model.Product;
import de.ecom.ecomapplication.model.User;
import de.ecom.ecomapplication.repository.CardItemRepository;
import de.ecom.ecomapplication.repository.ProductRepository;
import de.ecom.ecomapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CardItemRepository cardItemRepository;

    public boolean addToCart(String userId, CartItemRequest cartRequest) {
        // fetch the product
        Optional<Product> productOpt = productRepository.findById(cartRequest.getProductId());
        if (productOpt.isEmpty())
            return false;
        Product product = productOpt.get();

        // check the quantity
        if (product.getQuantity() < cartRequest.getQuantity())
            return false;

        // fetch the user
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty())
            return false;
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

        return true;
    }
}
