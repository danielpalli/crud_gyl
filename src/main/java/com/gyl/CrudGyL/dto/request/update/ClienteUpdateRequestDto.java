package com.gyl.CrudGyL.dto.request.update;

import com.gyl.CrudGyL.enums.Genero;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
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
    String direccion,

    @Size(max = 10, message = "El DNI no puede tener más de 10 caracteres")
    @Pattern(regexp = "\\d+", message = "El DNI debe ser un número positivo")
    String dni,

    Genero genero,

    @Size(max = 50, message = "La nacionalidad no puede tener más de 50 caracteres")
    String nacionalidad
) {}
