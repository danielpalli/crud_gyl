package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;

import java.util.List;

public interface VentaService {
    VentaResponseDto crear(VentaRequestDto dto);

    List<VentaResponseDto> listar();

    VentaResponseDto buscarPorId(Long id);

    VentaResponseDto actualizar(Long id, VentaRequestDto dto);

    void eliminar(Long id);
}
