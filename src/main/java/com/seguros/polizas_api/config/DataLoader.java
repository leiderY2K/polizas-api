package com.seguros.polizas_api.config;

import com.seguros.polizas_api.entity.*;
import com.seguros.polizas_api.repository.PolizaRepository;
import com.seguros.polizas_api.repository.RiesgoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatos(
            PolizaRepository polizaRepository,
            RiesgoRepository riesgoRepository) {

        return args -> {
            Poliza individual = new Poliza();
            individual.setTipo(TipoPoliza.INDIVIDUAL);
            individual.setEstado(EstadoPoliza.ACTIVA);
            individual.setCanon(new BigDecimal("1000000.00"));
            individual.setPrima(new BigDecimal("12000000.00"));

            polizaRepository.save(individual);

            Riesgo riesgoIndividual = new Riesgo();
            riesgoIndividual.setDescripcion("Apartamento 101");
            riesgoIndividual.setEstado(EstadoRiesgo.ACTIVO);
            riesgoIndividual.setPoliza(individual);
            riesgoRepository.save(riesgoIndividual);

            Poliza colectiva = new Poliza();
            colectiva.setTipo(TipoPoliza.COLECTIVA);
            colectiva.setEstado(EstadoPoliza.ACTIVA);
            colectiva.setCanon(new BigDecimal("1500000.00"));
            colectiva.setPrima(new BigDecimal("18000000.00"));
            polizaRepository.save(colectiva);

            Riesgo riesgoColectivo = new Riesgo();
            riesgoColectivo.setDescripcion("Apartamento 201");
            riesgoColectivo.setEstado(EstadoRiesgo.ACTIVO);
            riesgoColectivo.setPoliza(colectiva);
            riesgoRepository.save(riesgoColectivo);
        };
    }
}