package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.entity.TipoProducto;
import com.gyl.CrudGyL.exception.RecursoNoEncontradoException;
import com.gyl.CrudGyL.mapper.TipoProductoMapper;
import com.gyl.CrudGyL.repository.TipoProductoRepository;
import com.gyl.CrudGyL.service.TipoProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoProductoServiceImpl implements TipoProductoService {

    private TipoProductoRepository repository;

    public TipoProductoServiceImpl(TipoProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoProductoResponseDto crear(TipoProductoRequestDto dto) {
        TipoProducto tipoProducto = TipoProductoMapper.toEntity(dto);
        TipoProducto guardado = repository.save(tipoProducto);
        return TipoProductoMapper.toResponseDto(guardado);
    }

    @Override
    public List<TipoProductoResponseDto> listar() {
        return repository.findAll()
            .stream()
            .map(TipoProductoMapper::toResponseDto)
            .toList();
    }

    @Override
    public TipoProductoResponseDto buscarPorId(Long id) {
        return repository.findById(id)
            .map(TipoProductoMapper::toResponseDto)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el tipo de producto con id: " + id
            ));
    }

    @Override
    public TipoProductoResponseDto actualizar(Long id, TipoProductoRequestDto dto) {
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el tipo de producto con id: " + id
            ));

        TipoProductoMapper.updateEntity(tipoProducto, dto);
        TipoProducto guardado = repository.save(tipoProducto);
        return TipoProductoMapper.toResponseDto(guardado);
    }

    @Override
    public void eliminar(Long id) {
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el tipo de producto con id: " + id
            ));

        repository.delete(tipoProducto);
    }
}
