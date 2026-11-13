package com.seguros.polizas_api.dto;

import jakarta.validation.constraints.NotBlank;

public record CrearRiesgoRequest(
        @NotBlank String descripcion
) {

}
