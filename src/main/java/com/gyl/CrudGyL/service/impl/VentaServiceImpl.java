package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import com.gyl.CrudGyL.service.VentaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {


    @Override
    public VentaResponseDto crear(VentaRequestDto dto) {
        return null;
    }

    @Override
    public List<VentaResponseDto> listar() {
        return List.of();
    }

    @Override
    public VentaResponseDto buscarPorId(Long id) {
        return null;
    }

    @Override
    public VentaResponseDto actualizar(Long id, VentaRequestDto dto) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }
}
