package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.ProductoRequestDto;
import com.gyl.CrudGyL.entity.Producto;
import com.gyl.CrudGyL.dto.response.ProductoResponseDto;

public class ProductoMapper {
    private ProductoMapper() {}

    public static Producto toEntity(ProductoRequestDto dto){
        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
        return producto;
    }

    public static ProductoResponseDto toResponseDto(Producto producto){
        return new ProductoResponseDto(
            producto.getIdProducto(),
            producto.getNombre(),
            producto.getPrecio(),
            producto.getStock()
        );
    }

    public static void updateEntity(Producto producto, ProductoRequestDto dto){
        producto.setNombre(dto.nombre());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
    }
}