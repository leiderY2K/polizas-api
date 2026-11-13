package com.seguros.polizas_api.service;

import com.seguros.polizas_api.entity.*;
import com.seguros.polizas_api.repository.PolizaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PolizaService {

    private final PolizaRepository polizaRepository;

    public PolizaService(PolizaRepository polizaRepository) {
        this.polizaRepository = polizaRepository;
    }

    public List<Poliza> listar (TipoPoliza tipo, EstadoPoliza estado){
        if (tipo != null && estado != null){
            return  polizaRepository.findByTipoAndEstado(tipo,estado);
        }

        if(tipo != null){
            return  polizaRepository.findByTipo(tipo);
        }

        if(estado != null){
            return polizaRepository.findByEstado(estado);
        }

        return polizaRepository.findAll();

    }

    public Poliza buscarPorId(long id){
        return polizaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrad: " + id ));
    }

    @Transactional
    public Poliza renovar(Long id, double ipc){
        Poliza poliza = buscarPorId(id);

        if (poliza.getEstado() == EstadoPoliza.CANCELADA){
            throw new IllegalStateException("No se puede renovar una póliza cancelada");
        }

        if(!Double.isFinite(ipc) || ipc < 0){
            throw new IllegalArgumentException("IPC invalido");
        }

        BigDecimal factor = BigDecimal.ONE.add(
                BigDecimal.valueOf(ipc).divide(BigDecimal.valueOf(100))
        );

        poliza.setCanon(poliza.getCanon().multiply(factor));
        poliza.setPrima(poliza.getCanon().multiply(factor));
        poliza.setEstado(EstadoPoliza.RENOVADA);

        return polizaRepository.save(poliza);
    }

    @Transactional
    public Poliza cancelar(Long id){
        Poliza poliza = buscarPorId(id);

        if (poliza.getEstado() == EstadoPoliza.CANCELADA){
            throw new IllegalStateException("La póliza ya esta cancelada");
        }

        poliza.setEstado(EstadoPoliza.CANCELADA);

        for(Riesgo riesgo : poliza.getRiesgos()){
            riesgo.setEstado(EstadoRiesgo.CANCELADO);
        }
        return polizaRepository.save(poliza);

    }
}
