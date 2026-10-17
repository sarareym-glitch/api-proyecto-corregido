package com.example.api.service;

import com.example.api.model.Product;
import com.example.api.model.ProductRequest;
import com.example.api.model.ProductResponse;
import com.example.api.repository.ProductRepository;
import com.example.api.utils.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio del módulo de productos (CRUD completo).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Lista todos los productos.
     *
     * @return lista de productos (vacía si no hay registros)
     */
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    /**
     * Consulta un producto por su identificador.
     *
     * @param id identificador del producto
     * @return el producto encontrado
     * @throws ProductNotFoundException si no existe
     */
    public ProductResponse getProductById(Long id) {
        return ProductResponse.from(findOrThrow(id));
    }

    /**
     * Crea un producto nuevo.
     *
     * @param request datos del producto (ya validados)
     * @return el producto guardado, con su id asignado
     */
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creando producto: {}", request.getName());
        Product product = Product.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        return ProductResponse.from(productRepository.save(product));
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id      identificador del producto a actualizar
     * @param request nuevos datos del producto
     * @return el producto actualizado
     * @throws ProductNotFoundException si no existe
     */
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existing = findOrThrow(id);
        log.info("Actualizando producto con id {}", id);
        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        return ProductResponse.from(productRepository.save(existing));
    }

    /**
     * Elimina un producto.
     *
     * @param id identificador del producto a eliminar
     * @throws ProductNotFoundException si no existe
     */
    public void deleteProduct(Long id) {
        Product existing = findOrThrow(id);
        log.info("Eliminando producto con id {}", id);
        productRepository.delete(existing);
    }

    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con id " + id));
    }
}
