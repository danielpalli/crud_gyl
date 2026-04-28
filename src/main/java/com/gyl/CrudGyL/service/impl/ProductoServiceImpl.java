package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.ProductoRequestDto;
import com.gyl.CrudGyL.dto.response.ProductoResponseDto;
import com.gyl.CrudGyL.entity.Producto;
import com.gyl.CrudGyL.exception.RecursoNoEncontradoException;
import com.gyl.CrudGyL.mapper.ProductoMapper;
import com.gyl.CrudGyL.repository.ProductoRepository;
import com.gyl.CrudGyL.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoResponseDto crear(ProductoRequestDto dto) {
        Producto producto = ProductoMapper.toEntity(dto);
        Producto guardado = productoRepository.save(producto);
        return ProductoMapper.toResponseDto(guardado);
    }

    @Override
    public List<ProductoResponseDto> listar() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductoResponseDto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .map(ProductoMapper::toResponseDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el id: " + id
                ));
    }

    @Override
    public List<ProductoResponseDto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(ProductoMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductoResponseDto actualizar(Long id, ProductoRequestDto dto){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el id" + id
                ));

        ProductoMapper.updateEntity(producto,dto);
        Producto guardado = productoRepository.save(producto);
        return ProductoMapper.toResponseDto(guardado);

    }

    @Override
    public void eliminar(Long id){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el id" + id
                ));

        productoRepository.delete(producto);
    }
}
