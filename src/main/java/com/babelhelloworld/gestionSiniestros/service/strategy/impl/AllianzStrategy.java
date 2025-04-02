package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AllianzStrategy extends BaseStrategy {

    private final Aseguradora aseguradora;
    private final AmortizacionService amortizacionService;

    public AllianzStrategy(@Qualifier("allianzAmortizacionService") AmortizacionService amortizacionService) {
        this.amortizacionService = amortizacionService;
        this.aseguradora = Aseguradora.ALLIANZ;
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
        double aumento;

        if (valor == bien.getValorCompra()) {
            aumento = 0;
        } else {
            aumento = aseguradora.getTasaAumento();
        }

        resultado = (valor * (1 + aumento));


        return resultado;
    }

}
