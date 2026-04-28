package com.gyl.CrudGyL.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_producto")
    private TipoProducto tipoProducto;
}
