package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.entity.Venta;
import com.gyl.CrudGyL.exception.RecursoNoEncontradoException;
import com.gyl.CrudGyL.mapper.VentaMapper;
import com.gyl.CrudGyL.repository.ClienteRepository;
import com.gyl.CrudGyL.repository.VentaRepository;
import com.gyl.CrudGyL.service.VentaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {
    private VentaRepository repository;
    private ClienteRepository clienteRepository;

    public VentaServiceImpl(VentaRepository repository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public VentaResponseDto crear(VentaRequestDto dto) {
        Venta venta = VentaMapper.toEntity(dto);
        Cliente cliente = clienteRepository.findById(dto.idCliente()).orElse(null);
        venta.setCliente(cliente);

        Venta guardado = repository.save(venta);
        return VentaMapper.toResponseDto(guardado);
    }

    @Override
    public List<VentaResponseDto> listar() {
        return repository.findAll()
            .stream()
            .map(VentaMapper::toResponseDto)
            .toList();
    }

    @Override
    public VentaResponseDto buscarPorId(Long id) {
        return repository.findById(id)
            .map(VentaMapper::toResponseDto)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontro el id: " + id
            ));
    }

    @Override
    public VentaResponseDto actualizar(Long id, VentaRequestDto dto) {
        Venta venta = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontro el id" + id
            ));

        VentaMapper.updateEntity(venta, dto);
        Cliente cliente = clienteRepository.findById(dto.idCliente()).orElse(null);
        venta.setCliente(cliente);

        Venta guardado = repository.save(venta);
        return VentaMapper.toResponseDto(guardado);
    }

    @Override
    public void eliminar(Long id) {
        Venta venta = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontro el id" + id
            ));

        repository.delete(venta);
    }
}
