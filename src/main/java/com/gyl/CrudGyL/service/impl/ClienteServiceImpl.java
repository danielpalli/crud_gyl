package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.exception.RecursoNoEncontradoException;
import com.gyl.CrudGyL.mapper.ClienteMapper;
import com.gyl.CrudGyL.repository.ClienteRepository;
import com.gyl.CrudGyL.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    @Override
    @Transactional
    public ClienteResponseDto crear(ClienteRequestDto dto) {
        if (repository.existsByCorreo(dto.correo())) {
            throw new IllegalArgumentException(
                "Ya existe un cliente con el correo: " + dto.correo()
            );
        }

        Cliente cliente = mapper.toEntity(dto);
        Cliente guardado = repository.save(cliente);

        return mapper.toDto(guardado);
    }

    @Override
    public List<ClienteResponseDto> listar() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public ClienteResponseDto buscarPorId(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el id: " + id
            ));
    }

    @Override
    @Transactional
    public ClienteResponseDto actualizar(Long id, ClienteRequestDto dto) {
        Cliente existeCliente = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el id" + id
            ));

        if (repository.existsByCorreoAndIdClienteNot(dto.correo(), id)) {
            throw new IllegalArgumentException(
                "Ya existe un cliente con el correo: " + dto.correo()
            );
        }

        mapper.updateEntity(existeCliente,dto);
        Cliente clienteActualizado = repository.save(existeCliente);
        return mapper.toDto(clienteActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException(
                "No se encontró el id: " + id
            ));

        repository.delete(cliente);
    }
}
