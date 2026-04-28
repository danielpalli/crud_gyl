package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDto(
    @NotBlank(message = "El nombre no puede estar vacio")
    String nombre,

    @NotBlank(message = "El nombre no puede estar vacio")
    String apellido,

    @Email(message = "Debe ser un correo electronico valido")
    String correo,

    @NotBlank(message = "El telefono no puede estar vacio")
    String telefono,

    @NotBlank(message = "La dirreccion no puede estar vacio")
    String dirreccion
    ) {
}
