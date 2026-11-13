package com.seguros.polizas_api.dto;

import com.seguros.polizas_api.entity.Poliza;

import java.math.BigDecimal;

public record PolizaResponse(
        Long id,
        String tipo,
        String estado,
        BigDecimal canon,
        BigDecimal prima
) {
    public static PolizaResponse from(Poliza poliza) {
        return new PolizaResponse(
                poliza.getId(),
                poliza.getTipo().name(),
                poliza.getEstado().name(),
                poliza.getCanon(),
                poliza.getPrima()
        );
    }
}