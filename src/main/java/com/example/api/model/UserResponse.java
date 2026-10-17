package com.example.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida de un usuario. Por diseño NO contiene la contraseña,
 * por lo que ninguna respuesta de la API puede filtrarla.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    /** Identificador del usuario. */
    private Long id;

    /** Nombre del usuario. */
    private String name;

    /** Correo electrónico del usuario. */
    private String email;

    /** Rol del usuario. */
    private String role;

    /**
     * Convierte una entidad {@link User} en su DTO de respuesta (sin contraseña).
     *
     * @param u entidad a convertir
     * @return DTO con los datos públicos del usuario
     */
    public static UserResponse from(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole())
                .build();
    }
}
