package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequestDto(
    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre,

    @NotBlank(message = "El nombre no puede estar vacío")
    String apellido,

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "Debe ser un correo electrónico válido")
    String correo,

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(min = 8, max = 10, message = "El teléfono debe tener entre 8 y 10 dígitos")
    @Pattern(regexp = "^[0-9]+$", message = "El teléfono solo puede contener números")
    String telefono,

    @NotBlank(message = "La dirección no puede estar vacía")
    String direccion
    ) {
}
