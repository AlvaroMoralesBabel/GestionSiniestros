package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;

public class MapfreStrategy extends BaseStrategy {

    public MapfreStrategy(Aseguradora aseguradora, AmortizacionService amortizacionService) {
        super(aseguradora, amortizacionService);
    }

    @Override
    public double calcularValorReal(Siniestro siniestro) {
        double anios = getAniosTranscurridos(bien, siniestro);

        double aniosAjustados = calcularAniosUso(anios, aseguradora.isUsaAniosProporcionales());
        aniosAjustados = aplicarPrimerAnio(aniosAjustados, aseguradora.isCuentaPrimerAnio());

        int amort = aplicarMultiplicador(amortizacionService.obtenerAniosAmortizacion(aseguradora, bien.getTipo()), aseguradora.getMultiplicadorAmortizacion());
        double porcAnual = 1.0 / amort;

        double depreciacion = bien.getValorCompra() * porcAnual * aniosAjustados;
        double valor = bien.getValorCompra() - depreciacion;

        return aplicarValorResidual(bien.getValorCompra(), valor);
    }
}
