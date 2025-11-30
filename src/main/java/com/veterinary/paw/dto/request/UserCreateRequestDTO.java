package com.veterinary.paw.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserCreateRequestDTO (
        @NotBlank(message = "El email no puede estar vacío")
        @Size(max = 255, message = "El email debe tener como máximo 255 caracteres")
        @Email(message = "El formato del email es incorrecto")
        String email,

        @NotNull(message = "El DNI no puede ser nulo")
        @NotBlank(message = "El DNI no puede estar vacío")
        @Size(max = 20, message = "El DNI debe tener como máximo 20 caracteres")
        String dni,

        @JsonProperty("phone_number")
        @NotNull(message = "El número de teléfono no puede ser nulo")
        @NotBlank(message = "El número de teléfono no puede estar vacío")
        @Size(max = 20, message = "El número de teléfono debe tener como máximo 20 caracteres")
        String phoneNumber,

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        String password
) {
}
