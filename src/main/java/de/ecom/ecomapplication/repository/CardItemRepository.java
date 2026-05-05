package de.ecom.ecomapplication.repository;

import de.ecom.ecomapplication.model.CardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardItemRepository extends JpaRepository<CardItem, Long> {
}
