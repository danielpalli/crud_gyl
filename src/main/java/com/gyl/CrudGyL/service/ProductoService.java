package com.gyl.CrudGyL.service;

import com.gyl.CrudGyL.dto.request.ProductoRequestDto;
import com.gyl.CrudGyL.dto.request.update.ProductoUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.ProductoResponseDto;

import com.gyl.CrudGyL.dto.response.EstadoResponseDto;

import java.util.List;

public interface ProductoService {

    ProductoResponseDto crear(ProductoRequestDto dto);

    List<ProductoResponseDto> listar(String estado);

    ProductoResponseDto buscarPorId(Long id);

    List<ProductoResponseDto> buscarPorNombre(String nombre);

    ProductoResponseDto actualizar(Long id, ProductoUpdateRequestDto dto);

    EstadoResponseDto eliminar(Long id);

    EstadoResponseDto restaurar(Long id);
}
