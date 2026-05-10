package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import com.gyl.CrudGyL.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<VentaResponseDto> listar(@RequestParam(defaultValue = "todos") String estado) {
        return ventaService.listar(estado);
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
}
