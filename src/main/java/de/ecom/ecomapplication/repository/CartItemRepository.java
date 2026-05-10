package de.ecom.ecomapplication.repository;

import de.ecom.ecomapplication.model.CartItem;
import de.ecom.ecomapplication.model.Product;
import de.ecom.ecomapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);

    List<CartItem> findByUser(User user);

    void deleteByUserAndProduct(User user, Product product);
}
