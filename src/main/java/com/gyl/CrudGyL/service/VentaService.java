package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.ResumenVentasResponseDto;
import com.gyl.CrudGyL.dto.response.TopProductoResponseDto;
import com.gyl.CrudGyL.dto.response.VentaHistorialClienteResponseDto;
import com.gyl.CrudGyL.dto.response.VentaHistorialResponseDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {
    VentaResponseDto crear(VentaRequestDto dto);

    PageResponseDto<VentaResponseDto> listar(String estado, Pageable paginacion);

    VentaResponseDto buscarPorId(Long id);

    VentaResponseDto actualizar(Long id, VentaRequestDto dto);

    EstadoResponseDto anular(Long id);

    ResumenVentasResponseDto obtenerResumen();

    VentaHistorialClienteResponseDto obtenerHistorialCliente(Long idCliente, Pageable paginacion);

    VentaHistorialResponseDto obtenerVentasPorRango(LocalDate inicio, LocalDate fin, Pageable paginacion);

    List<TopProductoResponseDto> obtenerTopProductosMasVendidos();
}
