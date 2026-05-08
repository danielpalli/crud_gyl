package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ClienteRequestDto(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    String nombre,

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    String apellido,

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "Debe ser un correo electrónico válido")
    @Size(max = 150, message = "El correo electrónico no puede tener más de 150 caracteres")
    String correo,

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^(\\d{8}|\\d{10})$", message = "El teléfono solo deber tener 8 o 10 dígitos numéricos")
    String telefono,

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 150, message = "La dirección no puede tener más de 150 caracteres")
    String direccion
) {}
