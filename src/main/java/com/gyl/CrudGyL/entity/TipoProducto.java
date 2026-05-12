package com.gyl.CrudGyL.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name="tipo_producto")
@SQLDelete(sql = "UPDATE tipo_producto SET fecha_baja = UTC_TIMESTAMP() WHERE id_tipo_producto = ?")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoProducto extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoProducto;

    @Column(nullable = false, unique = true, length = 50)
    private String nombreTipoProducto;

    @Column(nullable = false, length = 100)
    private String descripcion;
}
