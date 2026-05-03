package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.entity.TipoProducto;

public class TipoProductoMapper {

    private TipoProductoMapper() {
    }

    public static TipoProducto toEntity(TipoProductoRequestDto dto) {
        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setNombre(dto.nombre());
        tipoProducto.setDescripcion(dto.descripcion());
        return tipoProducto;
    }

    public static TipoProductoResponseDto toResponseDto(TipoProducto tipoProducto) {
        return new TipoProductoResponseDto(
            tipoProducto.getIdTipoProducto(),
            tipoProducto.getNombre(),
            tipoProducto.getDescripcion()
        );
    }

    public static void updateEntity(TipoProducto tipoProducto, TipoProductoRequestDto dto) {
        tipoProducto.setNombre(dto.nombre());
        tipoProducto.setDescripcion(dto.descripcion());
    }
}
