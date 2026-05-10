package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.ResumenVentasResponseDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import org.springframework.data.domain.Pageable;

public interface VentaService {
    VentaResponseDto crear(VentaRequestDto dto);

    PageResponseDto<VentaResponseDto> listar(String estado, Pageable paginacion);

    VentaResponseDto buscarPorId(Long id);

    VentaResponseDto actualizar(Long id, VentaRequestDto dto);

    EstadoResponseDto anular(Long id);

    ResumenVentasResponseDto obtenerResumen();
}
