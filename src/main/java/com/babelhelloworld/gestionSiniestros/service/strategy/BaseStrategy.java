package com.babelhelloworld.gestionSiniestros.service.strategy;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;

import java.time.temporal.ChronoUnit;

public abstract class BaseStrategy implements ValoracionStrategy {

    protected Aseguradora aseguradora;
    protected AmortizacionService amortizacionService;

    public BaseStrategy(Aseguradora aseguradora, AmortizacionService amortizacionService) {
        this.aseguradora = aseguradora;
        this.amortizacionService = amortizacionService;
    }

    @Override
    public abstract double calcularValorReal(Siniestro siniestro);

    /**
     * Devuelve la diferencia en años (posiblemente con decimales)
     * entre la fechaCompra del bien y la fechaSiniestro.
     */
    protected double getAniosTranscurridos(Bien bien, Siniestro siniestro) {
        if (bien.getFechaCompra() == null || siniestro.getFechaSiniestro() == null) {
            return 0.0;
        }
        long daysBetween = ChronoUnit.DAYS.between(
                bien.getFechaCompra(),
                siniestro.getFechaSiniestro()
        );
        return daysBetween / 365.0;
    }

    /**
     * Si la compañía no usa años proporcionales, se trunca a entero.
     */
    protected double calcularAniosUso(double anios, boolean usaAniosProporcionales) {
        if (!usaAniosProporcionales) {
            return (int) anios;
        }
        return anios;
    }

    /**
     * Si se cuenta el primer año completo, y aniosUso < 1, forzamos a 1.0
     */
    protected double aplicarPrimerAnio(double anios, boolean cuentaPrimerAnio) {
        if (cuentaPrimerAnio && anios < 1) {
            return 1.0;
        }
        return anios;
    }

    /**
     * Depreciación acumulada año a año para 'añosEnteros'.
     */
    protected double depreciacionAcumulada(double valorInicial, double porcentajeAnual, int añosEnteros) {
        double valor = valorInicial;
        for (int i = 0; i < añosEnteros; i++) {
            valor -= (valor * porcentajeAnual);
        }
        return valor;
    }

    /**
     * Aplica un multiplicador a los años de amortización (ej. Mutua: *2).
     */
    protected int aplicarMultiplicador(int aniosAmortOriginal, int multiplicador) {
        return aniosAmortOriginal * multiplicador;
    }

    /**
     * Aplica valor residual según el % en la Aseguradora.
     */
    protected double aplicarValorResidual(double valorCompra, double valorCalculado) {
        double residualPct = aseguradora.getValorResidual();
        if (residualPct <= 0) {
            return valorCalculado;
        }
        double residual = valorCompra * residualPct;
        return Math.max(residual, valorCalculado);
    }
}
