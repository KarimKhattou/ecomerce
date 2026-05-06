package de.ecom.ecomapplication.repository;

import de.ecom.ecomapplication.model.CardItem;
import de.ecom.ecomapplication.model.Product;
import de.ecom.ecomapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardItemRepository extends JpaRepository<CardItem, Long> {
    CardItem findByUserAndProduct(User user, Product product);
}
