package com.seguros.polizas_api.repository;

import com.seguros.polizas_api.entity.EstadoPoliza;
import com.seguros.polizas_api.entity.Poliza;
import com.seguros.polizas_api.entity.TipoPoliza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolizaRepository extends JpaRepository<Poliza, Long> {

    List<Poliza> findByTipoAndEstado(
            TipoPoliza tipo,
            EstadoPoliza estado
    );

    List<Poliza> findByTipo(TipoPoliza tipo);

    List<Poliza> findByEstado(EstadoPoliza estado);
}
