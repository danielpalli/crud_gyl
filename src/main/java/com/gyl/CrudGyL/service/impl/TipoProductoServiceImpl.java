package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.TipoProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.TipoProductoUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.TipoProductoResponseDto;
import com.gyl.CrudGyL.entity.TipoProducto;
import com.gyl.CrudGyL.exception.BadRequestException;
import com.gyl.CrudGyL.exception.ConflictException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
import com.gyl.CrudGyL.mapper.TipoProductoMapper;
import com.gyl.CrudGyL.repository.ProductoRepository;
import com.gyl.CrudGyL.repository.TipoProductoRepository;
import com.gyl.CrudGyL.service.TipoProductoService;
import com.gyl.CrudGyL.specification.TipoProductoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageResponseDto<TipoProductoResponseDto> listar(String estado, String busqueda, Pageable paginacion) {
        if (!List.of("todos", "activos", "inactivos").contains(estado.toLowerCase())) {
            throw new BadRequestException("Estado inválido. Use: todos, activos o inactivos.");
        }

        Page<TipoProducto> page = repository.findAll(
                TipoProductoSpecification.conFiltros(estado, busqueda), paginacion);
        List<TipoProductoResponseDto> content = mapper.toDtoList(page.getContent());

        return PageResponseDto.<TipoProductoResponseDto>builder()
                .contenido(content)
                .numeroPagina(page.getNumber())
                .tamanioPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .esUltima(page.isLast())
                .build();
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
    public TipoProductoResponseDto actualizar(Long id, TipoProductoUpdateRequestDto dto) {
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
    public EstadoResponseDto eliminar(Long id) {
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el tipo de producto con id: " + id
            ));

        if (productoRepository.existsByTipoProductoIdTipoProducto(id)) {
            throw new ConflictException("No se puede eliminar el tipo de producto porque tiene prductos asociados.");
        }

        repository.delete(tipoProducto);
        
        return EstadoResponseDto.builder()
                .id(tipoProducto.getIdTipoProducto())
                .nombre(tipoProducto.getNombreTipoProducto())
                .mensaje("fue dado de baja")
                .estado("inactivo")
                .build();
    }

    @Override
    @Transactional
    public EstadoResponseDto restaurar(Long id) {
        repository.restaurarTipoProducto(id);
        TipoProducto tipoProducto = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el tipo de producto con id: " + id
            ));
            
        return EstadoResponseDto.builder()
                .id(tipoProducto.getIdTipoProducto())
                .nombre(tipoProducto.getNombreTipoProducto())
                .mensaje("fue dado de alta")
                .estado("activo")
                .build();
    }
}
