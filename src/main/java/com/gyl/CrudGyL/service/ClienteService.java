package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.request.update.ClienteUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponseDto crear(ClienteRequestDto dto);

    PageResponseDto<ClienteResponseDto> listar(String estado, String busqueda, Pageable paginacion);

    ClienteResponseDto buscarPorId(Long id);

    ClienteResponseDto actualizar(Long id, ClienteUpdateRequestDto dto);

    EstadoResponseDto eliminar(Long id);

    EstadoResponseDto restaurar(Long id);
}
