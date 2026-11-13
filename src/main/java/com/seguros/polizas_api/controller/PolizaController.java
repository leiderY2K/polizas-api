package com.seguros.polizas_api.controller;

import com.seguros.polizas_api.dto.PolizaResponse;
import com.seguros.polizas_api.dto.RenovarPolizaRequest;
import com.seguros.polizas_api.entity.EstadoPoliza;
import com.seguros.polizas_api.entity.TipoPoliza;
import com.seguros.polizas_api.service.PolizaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polizas")
public class PolizaController {

    private final PolizaService polizaService;

    public PolizaController(PolizaService polizaService) {
        this.polizaService = polizaService;
    }

    @GetMapping
    public List<PolizaResponse> listar(
            @RequestParam(required = false) TipoPoliza tipo,
            @RequestParam(required = false) EstadoPoliza estado) {

        return polizaService.listar(tipo, estado)
                .stream()
                .map(PolizaResponse::from)
                .toList();
    }

    @PostMapping("/{id}/renovar")
    public PolizaResponse renovar(
            @PathVariable Long id,
            @Valid @RequestBody RenovarPolizaRequest request) {

        return PolizaResponse.from(
                polizaService.renovar(id, request.ipc())
        );
    }

    @PostMapping("/{id}/cancelar")
    public PolizaResponse cancelar(@PathVariable Long id) {
        return PolizaResponse.from(polizaService.cancelar(id));
    }
}