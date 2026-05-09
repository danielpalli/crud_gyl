package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.request.update.ClienteUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.exception.ConflictException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
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
        validarCampos(dto.correo(), dto.dni(), null);

        Cliente cliente = mapper.toEntity(dto);
        Cliente nuevoCliente = repository.save(cliente);
        return mapper.toDto(nuevoCliente);
    }

    @Override
    public List<ClienteResponseDto> listar() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public ClienteResponseDto buscarPorId(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));
    }

    @Override
    @Transactional
    public ClienteResponseDto actualizar(Long id, ClienteUpdateRequestDto dto) {
        Cliente existeCliente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));

        validarCampos(dto.correo(), dto.dni(), id);

        mapper.updateEntity(existeCliente,dto);
        return mapper.toDto(existeCliente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));

        repository.delete(cliente);
    }

    private void validarCampos(String correo, String dni, Long idToExclude) {
        if (correo != null) {
            boolean correoExiste = (idToExclude == null)
                ? repository.existsByCorreo(correo)
                : repository.existsByCorreoAndIdClienteNot(correo, idToExclude);
            if (correoExiste) {
                throw new ConflictException("Ya existe un cliente con el correo: " + correo);
            }
        }

        if (dni != null) {
            boolean dniExiste = (idToExclude == null)
                ? repository.existsByDni(dni)
                : repository.existsByDniAndIdClienteNot(dni, idToExclude);
            if (dniExiste) {
                throw new ConflictException("Ya existe un cliente con el DNI: " + dni);
            }
        }
    }
}
