package com.example.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para crear o actualizar un producto (cuerpo JSON de POST y PUT).
 * Incluye las validaciones de los datos recibidos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    /** Nombre del producto: obligatorio, máximo 100 caracteres. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String name;

    /** Descripción opcional, máximo 255 caracteres. */
    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String description;

    /** Precio: obligatorio y no negativo. */
    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double price;

    /** Stock disponible: opcional, no negativo. */
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;
}
