package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapfreStrategy extends BaseStrategy {

    private final Aseguradora aseguradora;
    private final AmortizacionService amortizacionService;

    public MapfreStrategy(@Qualifier("mapfreAmortizacionService") AmortizacionService amortizacionService) {
        this.amortizacionService = amortizacionService;
        this.aseguradora = Aseguradora.MAPFRE;
    }

    @Override
    public Aseguradora getAseguradora() {
        return aseguradora;
    }

    @Override
    public Map<Bien, Double> calcularValorReal(Siniestro siniestro) {
        Map<Bien, Double> resultado = new HashMap<>();

        for (Bien bien : siniestro.getBienesAfectados()) {
            double anios = getAniosTranscurridos(bien, siniestro);
            double aniosAjustados = calcularAniosUso(anios, aseguradora.isUsaAniosProporcionales());
            aniosAjustados = aplicarPrimerAnio(aniosAjustados, aseguradora.isCuentaPrimerAnio());

            int amort = aplicarMultiplicador(
                    amortizacionService.obtenerAniosAmortizacion(bien),
                    aseguradora.getMultiplicadorAmortizacion()
            );
            double porcAnual = 1.0 / amort;

            double depreciacion = bien.getValorCompra() * porcAnual * aniosAjustados;
            double valor = bien.getValorCompra() - depreciacion;

            resultado.put(bien, aplicarValorResidual(bien.getValorCompra(), valor, aseguradora.getValorResidual()));
        }

        return resultado;
    }

}
