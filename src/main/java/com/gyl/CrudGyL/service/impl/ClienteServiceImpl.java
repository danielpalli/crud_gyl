package com.gyl.CrudGyL.service.impl;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.request.update.ClienteUpdateRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.dto.response.EstadoResponseDto;
import com.gyl.CrudGyL.dto.response.PageResponseDto;
import com.gyl.CrudGyL.entity.Cliente;
import com.gyl.CrudGyL.exception.BadRequestException;
import com.gyl.CrudGyL.exception.ConflictException;
import com.gyl.CrudGyL.exception.ResourceNotFoundException;
import com.gyl.CrudGyL.mapper.ClienteMapper;
import com.gyl.CrudGyL.repository.ClienteRepository;
import com.gyl.CrudGyL.service.ClienteService;
import com.gyl.CrudGyL.specification.ClienteSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageResponseDto<ClienteResponseDto> listar(String estado, String busqueda, Pageable paginacion) {
        if (!List.of("todos", "activos", "inactivos").contains(estado.toLowerCase())) {
            throw new BadRequestException("Estado inválido. Use: todos, activos o inactivos.");
        }

        Page<Cliente> page = repository.findAll(
                ClienteSpecification.conFiltros(estado, busqueda), paginacion);
        List<ClienteResponseDto> content = mapper.toDtoList(page.getContent());

        return PageResponseDto.<ClienteResponseDto>builder()
                .contenido(content)
                .numeroPagina(page.getNumber())
                .tamanioPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .esUltima(page.isLast())
                .build();
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
    public EstadoResponseDto eliminar(Long id) {
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));

        repository.delete(cliente);
        return EstadoResponseDto.builder()
                .id(cliente.getIdCliente())
                .nombre(cliente.getNombre())
                .mensaje("fue dado de baja")
                .estado("inactivo")
                .build();
    }

    @Override
    @Transactional
    public EstadoResponseDto restaurar(Long id) {
        repository.restaurarCliente(id);
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No se encontró el id: " + id
            ));
        return EstadoResponseDto.builder()
                .id(cliente.getIdCliente())
                .nombre(cliente.getNombre())
                .mensaje("fue dado de alta")
                .estado("activo")
                .build();
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
