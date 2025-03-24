package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import org.springframework.stereotype.Service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;

@Service
public class AllianzStrategy extends BaseStrategy {
    
    public AllianzStrategy(Aseguradora aseguradora) {
        super(aseguradora);
    }
    
    @Override
    public double calcularValorReal(Bien bien, double añosUso) {
        double añosAjustados = calcularAñosUso(añosUso, aseguradora.isUsaAniosProporcionales());
        añosAjustados = aplicarPrimerAño(añosAjustados, aseguradora.isCuentaPrimerAnio());
        int añosEnteros = (int) añosAjustados;

        int amortFinal = aplicarMultiplicador(bien.getAñosAmortizacion(), aseguradora.getMultiplicadorAmortizacion());
        double porcentajeAnual = 1.0 / amortFinal;

        double valor = depreciacionAcumulada(bien.getValorCompra(), porcentajeAnual, añosEnteros);

        return valor * (1 + aseguradora.getTasaAumento());
    }
}
