package com.example.api.repository;

import com.example.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos de {@link Product}.
 * Hereda de {@link JpaRepository} las operaciones CRUD (findAll, findById, save, delete).
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Indica si existe un producto con el nombre dado, sin distinguir mayúsculas.
     *
     * @param name nombre a buscar
     * @return {@code true} si existe
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Busca productos cuyo nombre contenga el texto dado, sin distinguir mayúsculas.
     *
     * @param name fragmento del nombre
     * @return lista de coincidencias
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
