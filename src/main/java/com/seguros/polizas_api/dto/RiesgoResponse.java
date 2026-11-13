package com.seguros.polizas_api.dto;

import com.seguros.polizas_api.entity.Riesgo;

public record RiesgoResponse(
        Long id,
        String descripcion,
        String estado,
        Long polizaId
) {
    public static RiesgoResponse from(Riesgo riesgo) {
        return new RiesgoResponse(
                riesgo.getId(),
                riesgo.getDescripcion(),
                riesgo.getEstado().name(),
                riesgo.getPoliza().getId()
        );
    }
}