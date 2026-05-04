package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.entity.TipoProducto;
import com.gyl.CrudGyL.mapper.config.GlobalMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface TipoProductoMapper {
    TipoProducto toEntity(TipoProductoRequestDto dto);
    TipoProductoResponseDto toDto(TipoProducto entity);
    List<TipoProductoResponseDto> toDtoList(List<TipoProducto> listEntity);
    void updateEntity(@MappingTarget TipoProducto entity, TipoProductoRequestDto dto);
}
