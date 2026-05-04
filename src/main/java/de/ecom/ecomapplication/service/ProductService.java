package de.ecom.ecomapplication.service;

import de.ecom.ecomapplication.dto.ProductRequest;
import de.ecom.ecomapplication.dto.ProductResponse;
import de.ecom.ecomapplication.model.Product;
import de.ecom.ecomapplication.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest productRequest) {
        return mapToResponse(productRepository.save(mapFromRequest(productRequest)));
    }

    private ProductResponse mapToResponse(Product savedProduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        productResponse.setQuantity(savedProduct.getQuantity());
        productResponse.setActive(savedProduct.getActive());
        return productResponse;
    }

    private Product mapFromRequest(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setImageUrl(productRequest.getImageUrl());
        return product;
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return  productRepository.findById(id)
                        .map(existingProduct -> {
                            existingProduct.setName(mapFromRequest(productRequest).getName());
                            existingProduct.setDescription(mapFromRequest(productRequest).getDescription());
                            existingProduct.setCategory(mapFromRequest(productRequest).getCategory());
                            existingProduct.setPrice(mapFromRequest(productRequest).getPrice());
                            existingProduct.setQuantity(mapFromRequest(productRequest).getQuantity());
                            existingProduct.setImageUrl(mapFromRequest(productRequest).getImageUrl());
                            productRepository.save(existingProduct);
                            return mapToResponse(existingProduct);
                        });
    }

    public List<ProductResponse> getAllProduct() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }
}

