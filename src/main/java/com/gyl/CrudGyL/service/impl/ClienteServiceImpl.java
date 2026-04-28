package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.exception.RecursoNoEncontradoException;
import com.gyl.CrudGyL.mapper.ClienteMapper;
import com.gyl.CrudGyL.repository.ClienteRepository;
import com.gyl.CrudGyL.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {
    private ClienteRepository repository;

    public ClienteServiceImpl(ClienteRepository repository){
        this.repository = repository;
    }

    @Override
    public ClienteResponseDto crear(ClienteRequestDto dto) {
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente guardado = repository.save(cliente);

        return ClienteMapper.toResponseDto(guardado);
    }

    @Override
    public List<ClienteResponseDto> listar() {
        return repository.findAll()
            .stream()
            .map(ClienteMapper::toResponseDto)
            .toList();
    }

    @Override
    public ClienteResponseDto buscarPorId(Long id) {
        return repository.findById(id)
            .map(ClienteMapper::toResponseDto)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el id: " + id
            ));
    }

    @Override
    public ClienteResponseDto actualizar(Long id, ClienteRequestDto dto) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el id" + id
            ));

        ClienteMapper.updateEntity(cliente,dto);
        Cliente guardado = repository.save(cliente);
        return ClienteMapper.toResponseDto(guardado);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el id" + id
            ));

        repository.delete(cliente);
    }
}
