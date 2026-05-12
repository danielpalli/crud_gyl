package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.ProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.ProductoUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.dto.response.ProductoResponseDto;
import com.gyl.CrudGyL.entity.Producto;
import com.gyl.CrudGyL.entity.TipoProducto;
import com.gyl.CrudGyL.exception.BadRequestException;
import com.gyl.CrudGyL.exception.ConflictException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
import com.gyl.CrudGyL.mapper.ProductoMapper;
import com.gyl.CrudGyL.repository.ProductoRepository;
import com.gyl.CrudGyL.repository.TipoProductoRepository;
import com.gyl.CrudGyL.service.ProductoService;
import com.gyl.CrudGyL.specification.ProductoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    @Transactional
    public ProductoResponseDto crear(ProductoRequestDto dto) {
        if (repository.existsByNombreProducto(dto.nombreProducto())) {
            throw new ConflictException("Ya existe un producto con el nombreProducto: " + dto.nombreProducto());
        }

        Producto producto = mapper.toEntity(dto);
        TipoProducto tipoProducto = buscarTipoProducto(dto.idTipoProducto());
        validarTipoProductoActivo(tipoProducto);
        producto.setTipoProducto(tipoProducto);
        Producto nuevoProducto = repository.save(producto);
        return mapper.toDto(nuevoProducto);
    }

    @Override
    public PageResponseDto<ProductoResponseDto> listar(String estado, String busqueda, Pageable paginacion) {
        if (!List.of("todos", "activos", "inactivos").contains(estado.toLowerCase())) {
            throw new BadRequestException("Estado inválido. Use: todos, activos o inactivos.");
        }

        Page<Producto> page = repository.findAll(
                ProductoSpecification.conFiltros(estado, busqueda), paginacion);
        List<ProductoResponseDto> content = mapper.toDtoList(page.getContent());

        return PageResponseDto.<ProductoResponseDto>builder()
                .contenido(content)
                .numeroPagina(page.getNumber())
                .tamanioPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .esUltima(page.isLast())
                .build();
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
        return mapper.toDtoList(repository.findByNombreProductoContainingIgnoreCase(nombre));
    }

    @Override
    @Transactional
    public ProductoResponseDto actualizar(Long id, ProductoUpdateRequestDto dto) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));

        if (repository.existsByNombreProductoAndIdProductoNot(dto.nombreProducto(), id)) {
            throw new ConflictException("Ya existe un producto con el nombreProducto: " + dto.nombreProducto());
        }

        mapper.updateEntity(producto, dto);
        if (dto.idTipoProducto() != null){
            TipoProducto tipoProducto = tipoProductoRepository.findById(dto.idTipoProducto())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "No se encontró el tipo de producto con id: " + dto.idTipoProducto()));
            validarTipoProductoActivo(tipoProducto);
            producto.setTipoProducto(tipoProducto);
            if (producto.getFechaBaja() == null) {
                producto.setEstadoProducto(true);
            }
        }
        return mapper.toDto(producto);
    }

    @Override
    @Transactional
    public EstadoResponseDto eliminar(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));

        repository.delete(producto);
        
        return EstadoResponseDto.builder()
                .id(producto.getIdProducto())
                .nombre(producto.getNombreProducto())
                .mensaje("fue dado de baja")
                .estado("inactivo")
                .build();
    }

    @Override
    @Transactional
    public EstadoResponseDto restaurar(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el id: " + id));

        validarTipoProductoActivo(producto.getTipoProducto());

        repository.restaurarProducto(id);
                        
        return EstadoResponseDto.builder()
                .id(producto.getIdProducto())
                .nombre(producto.getNombreProducto())
                .mensaje("fue dado de alta")
                .estado("activo")
                .build();
    }

    private TipoProducto buscarTipoProducto(Long idTipoProducto) {
        return tipoProductoRepository.findById(idTipoProducto)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el tipo de producto con id: " + idTipoProducto));
    }

    private void validarTipoProductoActivo(TipoProducto tipoProducto) {
        if (tipoProducto.getFechaBaja() != null) {
            throw new ConflictException("No se puede asociar un producto a un tipo de producto inactivo.");
        }
    }

}
