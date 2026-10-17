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
 * Entidad JPA que representa un producto de la tienda.
 * Se almacena en la tabla {@code productos}.
 * Se usa {@code @Getter}/{@code @Setter} (y no {@code @Data}) para evitar
 * problemas de equals/hashCode con los proxies de Hibernate.
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /** Identificador único generado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del producto (obligatorio, máximo 100 caracteres). */
    @Column(nullable = false, length = 100)
    private String name;

    /** Descripción opcional del producto. */
    private String description;

    /** Precio del producto (obligatorio, no negativo). */
    @Column(nullable = false)
    private Double price;

    /** Unidades disponibles en inventario. */
    private Integer stock;
}
