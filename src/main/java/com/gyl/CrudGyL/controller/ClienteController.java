package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.request.update.ClienteUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public PageResponseDto<ClienteResponseDto> listar(
            @RequestParam(defaultValue = "todos") String estado,
            @RequestParam(required = false) String busqueda,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam(defaultValue = "idCliente") String campoOrden,
            @RequestParam(defaultValue = "asc") String tipoOrden){
        
        Direction tipoDeOrden = tipoOrden.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;
        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by(tipoDeOrden, campoOrden));
        
        return clienteService.listar(estado, busqueda, paginacion);
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
