package com.seguros.polizas_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "riesgos")
@Getter
@Setter
public class Riesgo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private  EstadoRiesgo estado = EstadoRiesgo.ACTIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliza_id",nullable = false)
    private Poliza poliza;
}
