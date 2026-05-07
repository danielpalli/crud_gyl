package com.gyl.CrudGyL.dto.request.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequestDto(
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    String nombre,

    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    String apellido,

    @Email(message = "Debe ser un correo electrónico válido")
    @Size(max = 150, message = "El correo electrónico no puede tener más de 150 caracteres")
    String correo,

    @Pattern(regexp = "^(\\d{8}|\\d{10})$", message = "El teléfono solo deber tener 8 o 10 dígitos numéricos")
    String telefono,

    @Size(max = 150, message = "La dirección no puede tener más de 150 caracteres")
    String direccion
){}
