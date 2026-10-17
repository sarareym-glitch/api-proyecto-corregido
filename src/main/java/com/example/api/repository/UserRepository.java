package com.example.api.repository;

import com.example.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos de {@link User}.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Indica si ya existe un usuario con el correo dado.
     *
     * @param email correo a verificar
     * @return {@code true} si existe
     */
    boolean existsByEmail(String email);

    /**
     * Indica si otro usuario (distinto del indicado) ya usa el correo dado.
     * Permite que un usuario conserve su propio correo al actualizarse.
     *
     * @param email correo a verificar
     * @param id    identificador del usuario que se está actualizando
     * @return {@code true} si el correo pertenece a otro usuario
     */
    boolean existsByEmailAndIdNot(String email, Long id);
}
