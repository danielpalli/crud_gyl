package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.ProductoRequestDto;
import com.gyl.CrudGyL.dto.response.ProductoResponseDto;
import com.gyl.CrudGyL.entity.Producto;
import com.gyl.CrudGyL.entity.TipoProducto;
import com.gyl.CrudGyL.exception.ConflictException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
import com.gyl.CrudGyL.mapper.ProductoMapper;
import com.gyl.CrudGyL.repository.ProductoRepository;
import com.gyl.CrudGyL.repository.TipoProductoRepository;
import com.gyl.CrudGyL.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository repository;
    private final TipoProductoRepository tipoProductoRepository;
    private final ProductoMapper mapper;

    private TipoProducto resolverTipoProducto(Long idTipoProducto) {
        return tipoProductoRepository.findById(idTipoProducto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el tipo de producto con id: " + idTipoProducto));
    }

    @Override
    @Transactional
    public ProductoResponseDto crear(ProductoRequestDto dto) {
        if (repository.existsByNombre(dto.nombre())) {
            throw new ConflictException("Ya existe un producto con el nombre: " + dto.nombre());
        }

        Producto producto = mapper.toEntity(dto);
        producto.setTipoProducto(resolverTipoProducto(dto.idTipoProducto()));
        Producto nuevoProducto = repository.save(producto);
        return mapper.toDto(nuevoProducto);
    }

    @Override
    public List<ProductoResponseDto> listar() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public ProductoResponseDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));
    }

    @Override
    public List<ProductoResponseDto> buscarPorNombre(String nombre) {
        return mapper.toDtoList(repository.findByNombreContainingIgnoreCase(nombre));
    }

    @Override
    @Transactional
    public ProductoResponseDto actualizar(Long id, ProductoRequestDto dto) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));

        if (repository.existsByNombreAndIdProductoNot(dto.nombre(), id)) {
            throw new ConflictException("Ya existe un producto con el nombre: " + dto.nombre());
        }

        mapper.updateEntity(producto, dto);
        producto.setTipoProducto(resolverTipoProducto(dto.idTipoProducto()));
        Producto productoActualizado = repository.save(producto);
        return mapper.toDto(productoActualizado);

    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));

        repository.delete(producto);
    }
}
