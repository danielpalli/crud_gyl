package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.mapper.config.GlobalMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface ClienteMapper {
    Cliente toEntity(ClienteRequestDto dto);
    ClienteResponseDto toDto(Cliente entity);
    List<ClienteResponseDto> toDtoList(List<Cliente> listEntity);
    void updateEntity(@MappingTarget Cliente entity, ClienteRequestDto dto);
}
