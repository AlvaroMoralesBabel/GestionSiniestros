package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GeneralStrategy extends BaseStrategy {

    private final Aseguradora aseguradora;
    private final AmortizacionService amortizacionService;

    public GeneralStrategy(@Qualifier("generalAmortizacionService") AmortizacionService amortizacionService) {
        this.amortizacionService = amortizacionService;
        this.aseguradora = Aseguradora.GENERAL;
    }

    @Override
    public Aseguradora getAseguradora() {
        return aseguradora;
    }

    @Override
    public double calcularValorReal(Siniestro siniestro, int i) {
        validarSiniestro(siniestro);
        double resultado;

        Bien bien = siniestro.getBienesAfectados().get(i);
        validarBien(bien, siniestro);
        double anios = getAniosTranscurridos(bien, siniestro);
        double aniosAjustados = calcularAniosUso(anios, aseguradora.isUsaAniosProporcionales());
        aniosAjustados = aplicarPrimerAnio(aniosAjustados, aseguradora.isCuentaPrimerAnio());
        int aniosEnteros = (int) aniosAjustados;

        int amort = aplicarMultiplicador(
                amortizacionService.obtenerAniosAmortizacion(bien),
                aseguradora.getMultiplicadorAmortizacion()
        );
        double porcAnual = 1.0 / amort;

        double valor = depreciacionAcumulada(bien.getValorCompra(), porcAnual, aniosEnteros);
        resultado = (aplicarValorResidual(bien.getValorCompra(), valor, aseguradora.getValorResidual()));


        return resultado;
    }

}
