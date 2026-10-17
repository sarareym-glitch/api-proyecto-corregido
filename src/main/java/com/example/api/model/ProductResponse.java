package com.example.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida que expone los datos de un producto al cliente de la API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    /** Identificador del producto. */
    private Long id;

    /** Nombre del producto. */
    private String name;

    /** Descripción del producto. */
    private String description;

    /** Precio del producto. */
    private Double price;

    /** Stock disponible. */
    private Integer stock;

    /**
     * Convierte una entidad {@link Product} en su DTO de respuesta.
     *
     * @param p entidad a convertir
     * @return DTO con los datos del producto
     */
    public static ProductResponse from(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .build();
    }
}
