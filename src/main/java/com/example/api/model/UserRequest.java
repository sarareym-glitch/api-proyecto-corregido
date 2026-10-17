package com.example.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO de entrada para crear o actualizar un usuario.
 * La contraseña es obligatoria al crear y opcional al actualizar
 * (si no se envía, se conserva la actual).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    /** Nombre del usuario: obligatorio, máximo 80 caracteres. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    private String name;

    /** Correo electrónico: obligatorio, con formato válido y único. */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 120, message = "El correo no puede superar 120 caracteres")
    private String email;

    /** Contraseña en texto plano recibida del cliente (se cifra antes de guardar). */
    @Size(min = 6, max = 72, message = "La contraseña debe tener entre 6 y 72 caracteres")
    @ToString.Exclude
    private String password;

    /** Rol del usuario; si no se envía se asigna USER. */
    @Size(max = 30, message = "El rol no puede superar 30 caracteres")
    private String role;
}
