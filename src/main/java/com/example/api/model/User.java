package com.example.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA que representa un usuario del sistema.
 * Se almacena en la tabla {@code usuarios}. El correo es único y la
 * contraseña se guarda cifrada con BCrypt (nunca en texto plano).
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /** Identificador único generado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del usuario. */
    @Column(nullable = false, length = 80)
    private String name;

    /** Correo electrónico, único en toda la tabla. */
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    /** Hash BCrypt de la contraseña. Nunca se expone en la API. */
    @Column(nullable = false)
    private String password;

    /** Rol del usuario (por ejemplo USER o ADMIN). */
    private String role;
}
