package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.TipoProductoUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.service.TipoProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-productos")
@RequiredArgsConstructor
public class TipoProductoController {

    private final TipoProductoService tipoProductoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoProductoResponseDto crear(@Valid @RequestBody TipoProductoRequestDto dto) {
        return tipoProductoService.crear(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TipoProductoResponseDto> listar(@RequestParam(defaultValue = "todos") String estado) {
        return tipoProductoService.listar(estado);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TipoProductoResponseDto buscarPorId(@PathVariable Long id) {
        return tipoProductoService.buscarPorId(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TipoProductoResponseDto actualizar(@PathVariable Long id, @Valid @RequestBody TipoProductoUpdateRequestDto dto) {
        return tipoProductoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstadoResponseDto eliminar(@PathVariable Long id) {
        return tipoProductoService.eliminar(id);
    }

    @PatchMapping("/{id}/restaurar")
    @ResponseStatus(HttpStatus.OK)
    public EstadoResponseDto restaurar(@PathVariable Long id) {
        return tipoProductoService.restaurar(id);
    }
}
