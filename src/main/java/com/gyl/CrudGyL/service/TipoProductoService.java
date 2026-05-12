package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.TipoProductoUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import org.springframework.data.domain.Pageable;

public interface TipoProductoService {
    TipoProductoResponseDto crear(TipoProductoRequestDto dto);

    PageResponseDto<TipoProductoResponseDto> listar(String estado, String busqueda, Pageable paginacion);

    TipoProductoResponseDto buscarPorId(Long id);

    TipoProductoResponseDto actualizar(Long id, TipoProductoUpdateRequestDto dto);

    EstadoResponseDto eliminar(Long id);

    EstadoResponseDto restaurar(Long id);
}
