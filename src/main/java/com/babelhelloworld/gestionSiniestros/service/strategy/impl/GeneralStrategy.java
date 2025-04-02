package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;

import java.util.HashMap;
import java.util.Map;

public class GeneralStrategy extends BaseStrategy {

    public GeneralStrategy(Aseguradora aseguradora, AmortizacionService amortizacionService) {
        super(aseguradora, amortizacionService);
    }

    @Override
    public Map<Bien, Double> calcularValorReal(Siniestro siniestro) {
        Map<Bien, Double> resultado = new HashMap<>();

        for (Bien bien : siniestro.getBienesAfectados()) {
            double anios = getAniosTranscurridos(bien, siniestro);
            double aniosAjustados = calcularAniosUso(anios, aseguradora.isUsaAniosProporcionales());
            aniosAjustados = aplicarPrimerAnio(aniosAjustados, aseguradora.isCuentaPrimerAnio());
            int aniosEnteros = (int) aniosAjustados;

            int amort = aplicarMultiplicador(
                    amortizacionService.obtenerAniosAmortizacion(aseguradora, bien.getTipo()),
                    aseguradora.getMultiplicadorAmortizacion()
            );
            double porcAnual = 1.0 / amort;

            double valor = depreciacionAcumulada(bien.getValorCompra(), porcAnual, aniosEnteros);
            resultado.put(bien, aplicarValorResidual(bien.getValorCompra(), valor));
        }

        return resultado;
    }

}
