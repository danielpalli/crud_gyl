package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.service.ClienteService;
import jakarta.servlet.ServletRequest;
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
    public List<ClienteResponseDto> listar(){
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponseDto buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponseDto actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDto dto) {
        return  clienteService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void eliminar(@PathVariable Long id, ServletRequest servletRequest) {
        clienteService.eliminar(id);
    }
}
