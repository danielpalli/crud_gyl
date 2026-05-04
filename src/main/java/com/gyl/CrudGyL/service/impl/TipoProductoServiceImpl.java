package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.entity.TipoProducto;
import com.gyl.CrudGyL.exception.RecursoNoEncontradoException;
import com.gyl.CrudGyL.mapper.TipoProductoMapper;
import com.gyl.CrudGyL.repository.TipoProductoRepository;
import com.gyl.CrudGyL.service.TipoProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TipoProductoServiceImpl implements TipoProductoService {

    private final TipoProductoRepository repository;
    private final TipoProductoMapper mapper;

    @Override
    @Transactional
    public TipoProductoResponseDto crear(TipoProductoRequestDto dto) {
        if (repository.existsByNombre(dto.nombre())) {
            throw new IllegalArgumentException("Ya existe un tipo de producto con el nombre: " + dto.nombre());
        }

        TipoProducto tipoProducto = mapper.toEntity(dto);
        TipoProducto nuevoTipoProducto = repository.save(tipoProducto);
        return mapper.toDto(nuevoTipoProducto);
    }

    @Override
    public List<TipoProductoResponseDto> listar() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public TipoProductoResponseDto buscarPorId(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el tipo de producto con id: " + id
            ));
    }

    @Override
    @Transactional
    public TipoProductoResponseDto actualizar(Long id, TipoProductoRequestDto dto) {
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el tipo de producto con id: " + id
            ));

        if (repository.existsByNombreAndIdTipoProductoNot(dto.nombre(), id)) {
            throw new IllegalArgumentException("Ya existe un tipo de producto con el nombre: " + dto.nombre());
        }

        mapper.updateEntity(tipoProducto, dto);
        TipoProducto productoActualizado = repository.save(tipoProducto);
        return mapper.toDto(productoActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el tipo de producto con id: " + id
            ));

        repository.delete(tipoProducto);
    }
}
