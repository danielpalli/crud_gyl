package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;

import java.util.List;

public interface ClienteService {
    ClienteResponseDto crear(ClienteRequestDto dto);

    List<ClienteResponseDto> listar();

    ClienteResponseDto buscarPorId(Long id);

    ClienteResponseDto actualizar(Long id, ClienteRequestDto dto);

    void eliminar(Long id);
}
