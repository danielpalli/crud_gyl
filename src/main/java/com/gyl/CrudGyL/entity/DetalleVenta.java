package com.gyl.CrudGyL.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="detalle_venta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idDetalleVenta;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipo_producto")
    private Venta venta;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_producto")
    private Producto producto;
}
