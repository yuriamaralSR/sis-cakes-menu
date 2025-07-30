package com.asrcore.sis_cakes_menu.service;

import com.asrcore.sis_cakes_menu.exception.InconsistentDataException;
import com.asrcore.sis_cakes_menu.exception.ProductNotFoundException;
import com.asrcore.sis_cakes_menu.model.Product;
import com.asrcore.sis_cakes_menu.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        if (id == null) {
            throw new InconsistentDataException("Product id is null.");
        }
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Transactional
    public Product addProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new InconsistentDataException("Product name cannot be empty.");
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InconsistentDataException("Price must be greater than zero.");
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProductById(Long id) {
        if (id == null) {
            throw new InconsistentDataException("Product id is null.");
        }
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(id);
    }

    public Product updateProductById(Long id, Product product) {
        if (id == null) {
            throw new InconsistentDataException("Product id is null.");
        }
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Unable update Product."));
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new InconsistentDataException("Product name cannot be empty.");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InconsistentDataException("Price must be greater than zero.");
        }
        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setDescription(product.getDescription());
        return productRepository.save(productToUpdate);
    }
}
