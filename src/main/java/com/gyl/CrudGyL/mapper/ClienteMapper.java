package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.ClienteRequestDto;
import com.gyl.CrudGyL.dto.response.ClienteResponseDto;
import com.gyl.CrudGyL.entity.Cliente;

public class ClienteMapper {

    private ClienteMapper() {}

    public static Cliente toEntity(ClienteRequestDto dto){
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setCorreo(dto.correo());
        cliente.setDirreccion(dto.dirreccion());
        cliente.setTelefono(dto.telefono());
        return cliente;
    }

    public static ClienteResponseDto toResponseDto(Cliente cliente){
        return new ClienteResponseDto(
            cliente.getIdCliente(),
            cliente.getNombre(),
            cliente.getApellido(),
            cliente.getCorreo(),
            cliente.getDirreccion(),
            cliente.getTelefono()
        );
    }

    public static void updateEntity(Cliente cliente, ClienteRequestDto dto){
        cliente.setNombre(dto.nombre());
        cliente.setApellido(dto.apellido());
        cliente.setCorreo(dto.correo());
        cliente.setDirreccion(dto.dirreccion());
        cliente.setTelefono(dto.telefono());
    }
}
