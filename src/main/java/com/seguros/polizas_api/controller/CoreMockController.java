package com.seguros.polizas_api.controller;

import com.seguros.polizas_api.dto.CoreEventoRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

    private static final Logger log =
            LoggerFactory.getLogger(CoreMockController.class);

    @PostMapping("/evento")
    public ResponseEntity<Void> registrarEvento(
            @Valid @RequestBody CoreEventoRequest request) {

        log.info("Intento de envío al CORE. Evento: {}, Póliza: {}",
                request.evento(), request.polizaId());

        return ResponseEntity.ok().build();
    }
}