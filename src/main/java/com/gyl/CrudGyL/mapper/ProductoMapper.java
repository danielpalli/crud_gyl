package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.ProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.ProductoUpdateRequestDto;
import com.gyl.CrudGyL.entity.Producto;
import com.gyl.CrudGyL.dto.response.ProductoResponseDto;
import com.gyl.CrudGyL.mapper.config.GlobalMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface ProductoMapper {
    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "tipoProducto", ignore = true)
    @Mapping(target = "estadoProducto", ignore = true)
    Producto toEntity(ProductoRequestDto dto);

    @Mapping(source = "tipoProducto.idTipoProducto", target = "idTipoProducto")
    @Mapping(source = "tipoProducto.nombreTipoProducto", target = "nombreTipoProducto")
    ProductoResponseDto toDto(Producto entity);

    List<ProductoResponseDto> toDtoList(List<Producto> listEntity);

    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "tipoProducto", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "estadoProducto", ignore = true)
    void updateEntity(@MappingTarget Producto entity, ProductoUpdateRequestDto dto);
}
