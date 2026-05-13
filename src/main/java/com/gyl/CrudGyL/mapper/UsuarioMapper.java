package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.LoginRequestDto;
import com.gyl.CrudGyL.dto.request.RegistroRequestDto;
import com.gyl.CrudGyL.entity.Usuario;
import com.gyl.CrudGyL.mapper.config.GlobalMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "password", target = "password")
    Usuario toEntity(RegistroRequestDto dto, String password);

    void actualizarEntidad(@MappingTarget Usuario usuario, LoginRequestDto dto);
}
