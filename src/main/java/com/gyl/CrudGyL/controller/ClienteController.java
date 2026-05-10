package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.request.update.ClienteUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDto crear(@Valid @RequestBody ClienteRequestDto dto) {
        return clienteService.crear(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClienteResponseDto> listar(@RequestParam(defaultValue = "todos") String estado){
        return clienteService.listar(estado);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponseDto buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponseDto actualizar(@PathVariable Long id, @Valid @RequestBody ClienteUpdateRequestDto dto) {
        return  clienteService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstadoResponseDto eliminar(@PathVariable Long id) {
        return clienteService.eliminar(id);
    }

    @PatchMapping("/{id}/restaurar")
    @ResponseStatus(HttpStatus.OK)
    public EstadoResponseDto restaurar(@PathVariable Long id) {
        return clienteService.restaurar(id);
    }
}
