package de.ecom.ecomapplication.repository;

import de.ecom.ecomapplication.dto.ProductResponse;
import de.ecom.ecomapplication.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
}
