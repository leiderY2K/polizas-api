package com.seguros.polizas_api.controller;



import com.seguros.polizas_api.dto.CrearRiesgoRequest;
import com.seguros.polizas_api.dto.RiesgoResponse;
import com.seguros.polizas_api.service.RiesgoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RiesgoController {

    private final RiesgoService riesgoService;

    public RiesgoController(RiesgoService riesgoService) {
        this.riesgoService = riesgoService;
    }

    @GetMapping("/polizas/{id}/riesgos")
    public List<RiesgoResponse> listar(@PathVariable Long id) {
        return riesgoService.listarPorPoliza(id)
                .stream()
                .map(RiesgoResponse::from)
                .toList();
    }

    @PostMapping("/polizas/{id}/riesgos")
    public RiesgoResponse agregar(
            @PathVariable Long id,
            @Valid @RequestBody CrearRiesgoRequest request) {

        return RiesgoResponse.from(
                riesgoService.agregar(id, request.descripcion())
        );
    }

    @PostMapping("/riesgos/{id}/cancelar")
    public RiesgoResponse cancelar(@PathVariable Long id) {
        return RiesgoResponse.from(riesgoService.cancelar(id));
    }
}
