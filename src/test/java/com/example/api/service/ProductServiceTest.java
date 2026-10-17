package com.example.api.service;

import com.example.api.model.Product;
import com.example.api.model.ProductRequest;
import com.example.api.model.ProductResponse;
import com.example.api.repository.ProductRepository;
import com.example.api.utils.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de {@link ProductService} (sin base de datos).
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sample() {
        return Product.builder().id(1L).name("Teclado").description("Mecánico").price(189900.0).stock(15).build();
    }

    @Test
    void listaTodosLosProductos() {
        when(productRepository.findAll()).thenReturn(List.of(sample()));
        assertEquals(1, productService.getProducts().size());
    }

    @Test
    void consultaPorIdExistente() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sample()));
        assertEquals("Teclado", productService.getProductById(1L).getName());
    }

    @Test
    void consultaPorIdInexistenteLanzaNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    void creaProducto() {
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(7L);
            return p;
        });
        ProductResponse r = productService.createProduct(
                ProductRequest.builder().name(" Mouse ").price(50.0).stock(3).build());
        assertEquals(7L, r.getId());
        assertEquals("Mouse", r.getName());
    }

    @Test
    void actualizaProductoExistente() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sample()));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductResponse r = productService.updateProduct(1L,
                ProductRequest.builder().name("Teclado RGB").price(200000.0).stock(9).build());
        assertEquals("Teclado RGB", r.getName());
        assertEquals(9, r.getStock());
    }

    @Test
    void actualizarInexistenteLanzaNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(999L,
                ProductRequest.builder().name("x").price(1.0).build()));
    }

    @Test
    void eliminaProductoExistente() {
        Product p = sample();
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        productService.deleteProduct(1L);
        verify(productRepository).delete(p);
    }

    @Test
    void eliminarInexistenteLanzaNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(999L));
        verify(productRepository, never()).delete(any(Product.class));
    }
}
