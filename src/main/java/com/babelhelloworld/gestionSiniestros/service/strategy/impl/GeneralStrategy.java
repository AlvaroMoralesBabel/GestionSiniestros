package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;
import org.springframework.stereotype.Service;

@Service("GENERAL")
public class GeneralStrategy extends BaseStrategy {

    public GeneralStrategy(Aseguradora aseguradora) {
        super(aseguradora);
    }

    @Override
    public double calcularValorReal(Bien bien, double añosUso) {
        double añosAjustados = calcularAñosUso(añosUso, aseguradora.isUsaAniosProporcionales());
        int añosEnteros = (int) añosAjustados;

        int amortFinal = aplicarMultiplicador(bien.getAñosAmortizacion(), aseguradora.getMultiplicadorAmortizacion());
        double porcentajeAnual = 1.0 / amortFinal;

        double valor = depreciacionAcumulada(bien.getValorCompra(), porcentajeAnual, añosEnteros);
        return aplicarValorResidual(bien.getValorCompra(), valor);
    }

    @Override
    public Aseguradora getAseguradora() {
        return Aseguradora.GENERAL;
    }
}

