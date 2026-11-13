package com.seguros.polizas_api.dto;

import jakarta.validation.constraints.NotNull;

public record RenovarPolizaRequest(
        @NotNull Double ipc
) {
}