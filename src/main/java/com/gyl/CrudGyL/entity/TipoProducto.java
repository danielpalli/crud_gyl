package com.gyl.CrudGyL.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tipo_producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTipoProducto;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String descripcion;
}
