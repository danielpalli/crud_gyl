package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.ResumenVentasResponseDto;
import com.gyl.CrudGyL.dto.response.VentaHistorialClienteResponseDto;
import com.gyl.CrudGyL.dto.response.VentaHistorialResponseDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import com.gyl.CrudGyL.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService ventaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponseDto crear(@Valid @RequestBody VentaRequestDto dto) {
        return ventaService.crear(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<VentaResponseDto> listar(
            @RequestParam(defaultValue = "todas") String estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam(defaultValue = "idVenta") String campoOrden,
            @RequestParam(defaultValue = "asc") String tipoOrden) {
        
        Direction tipoDeOrden = tipoOrden.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;
        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by(tipoDeOrden, campoOrden));
        
        return ventaService.listar(estado, paginacion);
    }

    @GetMapping("/resumen")
    @ResponseStatus(HttpStatus.OK)
    public ResumenVentasResponseDto obtenerResumen() {
        return ventaService.obtenerResumen();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VentaResponseDto buscarPorId(@PathVariable Long id) {
        return ventaService.buscarPorId(id);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VentaResponseDto actualizar(@PathVariable Long id, @Valid @RequestBody VentaRequestDto dto) {
        return ventaService.actualizar(id, dto);
    }

    @PatchMapping("/{id}/anular")
    @ResponseStatus(HttpStatus.OK)
    public EstadoResponseDto anular(@PathVariable Long id) {
        return ventaService.anular(id);
    }

    @GetMapping("/cliente/{idCliente}")
    @ResponseStatus(HttpStatus.OK)
    public VentaHistorialClienteResponseDto obtenerHistorialCliente(
            @PathVariable Long idCliente,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio) {

        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by(Direction.DESC, "fechaVenta"));
        return ventaService.obtenerHistorialCliente(idCliente, paginacion);
    }

    @GetMapping("/rango")
    @ResponseStatus(HttpStatus.OK)
    public VentaHistorialResponseDto obtenerVentasPorRango(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam(defaultValue = "fechaVenta") String campoOrden,
            @RequestParam(defaultValue = "desc") String tipoOrden) {

        Direction tipoDeOrden = tipoOrden.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;
        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by(tipoDeOrden, campoOrden));
        return ventaService.obtenerVentasPorRango(inicio, fin, paginacion);
    }
}
