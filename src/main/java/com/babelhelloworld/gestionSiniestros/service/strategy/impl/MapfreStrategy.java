package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;

public class MapfreStrategy extends BaseStrategy {

    public MapfreStrategy(Aseguradora aseguradora) {
        super(aseguradora);
    }

    @Override
    public double calcularValorReal(Bien bien, Siniestro siniestro) {
        double anios = getAniosTranscurridos(bien, siniestro);

        double aniosAjustados = calcularAniosUso(anios, aseguradora.isUsaAniosProporcionales());
        aniosAjustados = aplicarPrimerAnio(aniosAjustados, aseguradora.isCuentaPrimerAnio());

        int amort = aplicarMultiplicador(bien.getAniosAmortizacion(), aseguradora.getMultiplicadorAmortizacion());
        double porcAnual = 1.0 / amort;

        double depreciacion = bien.getValorCompra() * porcAnual * aniosAjustados;
        double valor = bien.getValorCompra() - depreciacion;

        return aplicarValorResidual(bien.getValorCompra(), valor);
    }
}
