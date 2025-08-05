package com.asrcore.sis_cakes_menu.service;

import com.asrcore.sis_cakes_menu.exception.InconsistentDataException;
import com.asrcore.sis_cakes_menu.exception.ProductNotFoundException;
import com.asrcore.sis_cakes_menu.model.Product;
import com.asrcore.sis_cakes_menu.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach 
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts_existing_returnsAllProducts() {
        // Arrange
        Product product1 = new Product("Bolo de Morango", "Bolo sabor morango", new BigDecimal("14.90"), 10);
        Product product2 = new Product("Bolo de Laranja", "Bolo sabor laranja", new BigDecimal("10.90"), 5);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Bolo de Morango", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_validId_returnsProduct() {
        // Arrange
        Long id = 1L;
        Product product = new Product("Bolo de Morango", "Bolo sabor morango", new BigDecimal("14.90"), 10);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(id);

        // Assert
        assertNotNull(result);
        assertEquals("Bolo de Morango", result.getName());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testGetProductById_notFound_throwsProductNotFoundException() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testAddProduct_validProduct_returnsProduct() {
        // Arrange
        Product product = new Product("Bolo de Cenoura", "Bolo sabor cenoura coberto com chocolate", new BigDecimal("14.90"), 7);
        when(productRepository.save(product)).thenReturn(new Product("Bolo de Cenoura", "Bolo sabor cenoura coberto com chocolate", new BigDecimal("14.90"), 7));

        // Act
        Product savedProduct = productService.addProduct(product);

        // Assert
        assertNotNull(savedProduct);
        assertEquals("Bolo de Cenoura", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testAddProduct_nullProductName_throwsInconsistentDataException() {
        // Arrange
        Product product = new Product(null, " ", new BigDecimal("14.90"), 1);

        // Act & Assert
        InconsistentDataException exception = assertThrows(InconsistentDataException.class, () -> productService.addProduct(product));
        assertEquals("Product name cannot be empty.", exception.getMessage());
        verify(productRepository, never()).save(product);

    }

    @Test
    void testAddProduct_negativeProductPrice_throwsInconsistentDataException() {
        // Arrange
        Product product = new Product("Bolo no pote", " ", new BigDecimal("-14.90"), 1);

        // Act & Assert
        InconsistentDataException exception = assertThrows(InconsistentDataException.class, () -> productService.addProduct(product));
        assertEquals("Price must be greater than zero.", exception.getMessage());
        verify(productRepository, never()).save(product);

    }

    @Test
    void testUpdateProduct_validProduct_returnsProduct() {
        // Arrange
        Long id = 1L;
        Product existingProduct = new Product("Bolo de Chocolate", " ", new BigDecimal("14.90"), 1);
        Product updatedDetails = new Product("Bolo de Chocolate", "Sabor chocolate", new BigDecimal("14.90"), 7);
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(updatedDetails);
        // Act
        Product updatedProduct = productService.updateProductById(id, updatedDetails);
        //Assert
        assertNotNull(updatedProduct);
        assertEquals("Bolo de Chocolate", updatedProduct.getName());
        assertEquals("Sabor chocolate", updatedDetails.getDescription());
        assertEquals(7, updatedDetails.getQuantity());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProduct_productNotFound_throwsProductNotFoundException() {
        // Arrange
        Long id = 1L;
        Product updatedDetails = new Product("Bolo de Chocolate", " ", new BigDecimal("14.90"), 7);
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        // Act & Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.updateProductById(id, updatedDetails));
        assertEquals("Unable update product. Product not found.", exception.getMessage());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).save(updatedDetails);
    }

    @Test
    void testUpdateProduct_negativePrice_throwsProductNotFoundException() {
        // Arrange
        Long id = 1L;
        Product existingProduct = new Product("Bolo de Chocolate", " ", new BigDecimal("14.90"), 1);
        Product updatedDetails = new Product("Bolo de Chocolate", "Sabor chocolate", new BigDecimal("-14.90"), 7);
        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        // Act & Assert
        InconsistentDataException exception = assertThrows(InconsistentDataException.class, () -> productService.updateProductById(id, updatedDetails));
        assertEquals("Price must be greater than zero.", exception.getMessage());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).save(updatedDetails);
    }
}
