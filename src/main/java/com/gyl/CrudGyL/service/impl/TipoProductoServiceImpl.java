package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.entity.TipoProducto;
import com.gyl.CrudGyL.exception.ConflictException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
import com.gyl.CrudGyL.mapper.TipoProductoMapper;
import com.gyl.CrudGyL.repository.ProductoRepository;
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
    private final ProductoRepository productoRepository;
    private final TipoProductoMapper mapper;

    @Override
    @Transactional
    public TipoProductoResponseDto crear(TipoProductoRequestDto dto) {
        if (repository.existsByNombreTipoProducto(dto.nombreTipoProducto())) {
            throw new ConflictException("Ya existe un tipo de producto con el nombreProducto: " + dto.nombreTipoProducto());
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
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el tipo de producto con id: " + id
            ));
    }

    @Override
    @Transactional
    public TipoProductoResponseDto actualizar(Long id, TipoProductoRequestDto dto) {
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el tipo de producto con id: " + id
            ));

        if (dto.nombreTipoProducto() != null && repository.existsByNombreTipoProductoAndIdTipoProductoNot(dto.nombreTipoProducto(), id)) {
            throw new ConflictException("Ya existe un tipo de producto con el nombreProducto: " + dto.nombreTipoProducto());
        }

        mapper.updateEntity(tipoProducto, dto);
        return mapper.toDto(tipoProducto);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el tipo de producto con id: " + id
            ));

        if (productoRepository.existsByTipoProductoIdTipoProducto(id)) {
            throw new ConflictException("No se puede eliminar el tipo de producto porque tiene prductos asociados.");
        }

        repository.delete(tipoProducto);
    }
}
