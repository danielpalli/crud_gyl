package com.gyl.CrudGyL.controller;

import com.gyl.CrudGyL.dto.request.ProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.ProductoUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.ProductoResponseDto;
import com.gyl.CrudGyL.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDto crear(@Valid @RequestBody ProductoRequestDto dto) {
        return productoService.crear(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<ProductoResponseDto> listar(
            @RequestParam(defaultValue = "todos") String estado,
            @RequestParam(required = false) String busqueda,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam(defaultValue = "idProducto") String campoOrden,
            @RequestParam(defaultValue = "asc") String tipoOrden) {
        
        Direction tipoDeOrden = tipoOrden.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;
        Pageable paginacion = PageRequest.of(pagina, tamanio, Sort.by(tipoDeOrden, campoOrden));
        
        return productoService.listar(estado, busqueda, paginacion);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoResponseDto buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    @GetMapping("/buscar/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponseDto> buscarPorNombre(@PathVariable String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoResponseDto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoUpdateRequestDto dto) {
        return productoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstadoResponseDto eliminar(@PathVariable Long id) {
        return productoService.eliminar(id);
    }

    @PatchMapping("/{id}/restaurar")
    @ResponseStatus(HttpStatus.OK)
    public EstadoResponseDto restaurar(@PathVariable Long id) {
        return productoService.restaurar(id);
    }
}
