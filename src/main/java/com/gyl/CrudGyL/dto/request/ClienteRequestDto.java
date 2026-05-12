package com.gyl.CrudGyL.dto.request;

import com.gyl.CrudGyL.enums.Genero;
import jakarta.validation.constraints.*;
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
    String direccion,

    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(max = 10, message = "El DNI no puede tener más de 10 caracteres")
    @Pattern(regexp = "\\d+", message = "El DNI debe ser un número positivo")
    String dni,

    @NotNull(message = "El género es obligatorio")
    Genero genero,

    @NotBlank(message = "La nacionalidad no puede estar vacía")
    @Size(max = 50, message = "La nacionalidad no puede tener más de 50 caracteres")
    String nacionalidad
) {}
