package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.TipoProductoUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;

import com.gyl.CrudGyL.dto.response.EstadoResponseDto;

import java.util.List;

public interface TipoProductoService {
    TipoProductoResponseDto crear(TipoProductoRequestDto dto);

    List<TipoProductoResponseDto> listar(String estado);

    TipoProductoResponseDto buscarPorId(Long id);

    TipoProductoResponseDto actualizar(Long id, TipoProductoUpdateRequestDto dto);

    EstadoResponseDto eliminar(Long id);

    EstadoResponseDto restaurar(Long id);
}
