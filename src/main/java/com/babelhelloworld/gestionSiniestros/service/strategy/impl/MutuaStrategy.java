package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;

public class MutuaStrategy extends BaseStrategy {

    public MutuaStrategy(Aseguradora aseguradora) {
        super(aseguradora);
    }

    @Override
    public double calcularValorReal(Bien bien, Siniestro siniestro) {
        double anios = getAniosTranscurridos(bien, siniestro);
        double aniosAjustados = calcularAniosUso(anios, aseguradora.isUsaAniosProporcionales());
        aniosAjustados = aplicarPrimerAnio(aniosAjustados, aseguradora.isCuentaPrimerAnio());
        int aniosEnteros = (int) aniosAjustados;

        int amort = aplicarMultiplicador(bien.getAniosAmortizacion(), aseguradora.getMultiplicadorAmortizacion());
        double porcAnual = 1.0 / amort;

        double valor = depreciacionAcumulada(bien.getValorCompra(), porcAnual, aniosEnteros);
        return aplicarValorResidual(bien.getValorCompra(), valor);
    }
}
