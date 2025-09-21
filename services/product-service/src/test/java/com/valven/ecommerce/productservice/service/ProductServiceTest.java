package com.valven.ecommerce.productservice.service;

import com.valven.ecommerce.productservice.domain.Product;
import com.valven.ecommerce.productservice.exception.InsufficientStockException;
import com.valven.ecommerce.productservice.exception.ProductNotFoundException;
import com.valven.ecommerce.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku("TEST-001");
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStock(10);
        testProduct.setCategory("Electronics");
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        
        List<Product> result = productService.getAllProducts();

        
        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        
        Product result = productService.getProductById(1L);

        
        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(1L);
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        
        Product result = productService.createProduct(testProduct);

        
        assertNotNull(result);
        assertEquals(testProduct.getName(), result.getName());
        verify(productRepository).save(testProduct);
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProduct_WhenProductExists() {
        
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(new BigDecimal("149.99"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        
        Product result = productService.updateProduct(1L, updatedProduct);

        
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, testProduct));
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        
        when(productRepository.existsById(1L)).thenReturn(true);

        
        productService.deleteProduct(1L);

        
        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenProductNotFound() {
        
        when(productRepository.existsById(1L)).thenReturn(false);

        
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void reduceStock_ShouldReduceStock_WhenSufficientStock() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        
        productService.reduceStock(1L, 5);

        
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void reduceStock_ShouldThrowException_WhenInsufficientStock() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        
        assertThrows(InsufficientStockException.class, () -> productService.reduceStock(1L, 15));
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void addStock_ShouldAddStock_WhenValidQuantity() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        
        productService.addStock(1L, 5);

        
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void hasEnoughStock_ShouldReturnTrue_WhenSufficientStock() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        
        boolean result = productService.hasEnoughStock(1L, 5);

        
        assertTrue(result);
        verify(productRepository).findById(1L);
    }

    @Test
    void hasEnoughStock_ShouldReturnFalse_WhenInsufficientStock() {
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        
        boolean result = productService.hasEnoughStock(1L, 15);

        
        assertFalse(result);
        verify(productRepository).findById(1L);
    }

    @Test
    void searchProducts_ShouldReturnFilteredProducts() {
        
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(products);

        
        List<Product> result = productService.searchProducts("test");

        
        assertEquals(1, result.size());
        verify(productRepository).findByNameContainingIgnoreCase("test");
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsInCategory() {
        
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByCategory(anyString())).thenReturn(products);

        
        List<Product> result = productService.getProductsByCategory("Electronics");

        
        assertEquals(1, result.size());
        verify(productRepository).findByCategory("Electronics");
    }
}