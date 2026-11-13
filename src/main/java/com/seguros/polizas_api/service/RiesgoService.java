package com.seguros.polizas_api.service;

import com.seguros.polizas_api.entity.*;
import com.seguros.polizas_api.repository.RiesgoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiesgoService {
    private final RiesgoRepository riesgoRepository;
    private final PolizaService polizaService;


    public RiesgoService(RiesgoRepository riesgoRepository, PolizaService polizaService) {
        this.riesgoRepository = riesgoRepository;
        this.polizaService = polizaService;
    }

    public List<Riesgo> listarPorPoliza(Long polizaId){
        polizaService.buscarPorId(polizaId);

        return riesgoRepository.findByPolizaId(polizaId);
    }

    @Transactional
    public Riesgo agregar(Long polizaId, String descripcion){
        Poliza poliza = polizaService.buscarPorId(polizaId);

        if (poliza.getTipo() != TipoPoliza.COLECTIVA) {
            throw new IllegalStateException(
                    "Solo se pueden agregar riesgos a pólizas colectivas");
        }

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new IllegalStateException(
                    "No se pueden agregar riesgos a una póliza cancelada");
        }

        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción del riesgo es obligatoria");
        }

        Riesgo riesgo = new Riesgo();
        riesgo.setDescripcion(descripcion);
        riesgo.setEstado(EstadoRiesgo.ACTIVO);
        riesgo.setPoliza(poliza);

        return riesgoRepository.save(riesgo);
    }

    @Transactional
    public Riesgo cancelar (Long id){
        Riesgo riesgo = riesgoRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Riesgo no encontrado: " + id));

        if (riesgo.getEstado()== EstadoRiesgo.CANCELADO){
            throw new IllegalStateException("Riesgo ya esta cancelado");
        }

        riesgo.setEstado(EstadoRiesgo.CANCELADO);

        return riesgoRepository.save(riesgo);
    }
}
