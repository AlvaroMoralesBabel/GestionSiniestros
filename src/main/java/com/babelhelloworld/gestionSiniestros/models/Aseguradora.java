package com.babelhelloworld.gestionSiniestros.models;

import org.springframework.stereotype.Component;

@Component
public enum Aseguradora {
    GENERAL("General", 0.15, true, false, false, 0.0, 1),
    MAPFRE("Mapfre", 0.17, false, false, false, 0.0, 1),
    ALLIANZ("Allianz", 0.0, true, false, false, 0.05, 1),
    MUTUA("Mutua Madrileña", 0.10, true, true, true, 0.0, 2);

    private final String nombre;
    private final double valorResidual;
    private final boolean depreciacionAcumulada;
    private final boolean cuentaPrimerAnio;
    private final boolean usaAniosProporcionales;
    private final double tasaAumento;
    private final int multiplicadorAmortizacion;

    Aseguradora(
            String nombre,
            double valorResidual,
            boolean depreciacionAcumulada,
            boolean cuentaPrimerAnio,
            boolean usaAniosProporcionales,
            double tasaAumento,
            int multiplicadorAmortizacion
    ) {
        this.nombre = nombre;
        this.valorResidual = valorResidual;
        this.depreciacionAcumulada = depreciacionAcumulada;
        this.cuentaPrimerAnio = cuentaPrimerAnio;
        this.usaAniosProporcionales = usaAniosProporcionales;
        this.tasaAumento = tasaAumento;
        this.multiplicadorAmortizacion = multiplicadorAmortizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getValorResidual() {
        return valorResidual;
    }

    public boolean isDepreciacionAcumulada() {
        return depreciacionAcumulada;
    }

    public boolean isCuentaPrimerAnio() {
        return cuentaPrimerAnio;
    }

    public boolean isUsaAniosProporcionales() {
        return usaAniosProporcionales;
    }

    public double getTasaAumento() {
        return tasaAumento;
    }

    public int getMultiplicadorAmortizacion() {
        return multiplicadorAmortizacion;
    }
}
