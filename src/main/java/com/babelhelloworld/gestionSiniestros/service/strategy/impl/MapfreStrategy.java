package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.strategy.BaseStrategy;
import org.springframework.stereotype.Service;

@Service("MAPFRE")
public class MapfreStrategy extends BaseStrategy {

    public MapfreStrategy(Aseguradora aseguradora) {
        super(aseguradora);
    }

    @Override
    public double calcularValorReal(Bien bien, double añosUso) {
        double añosAjustados = calcularAñosUso(añosUso, false);
        int amortFinal = aplicarMultiplicador(bien.getAñosAmortizacion(), aseguradora.getMultiplicadorAmortizacion());

        double porcentajeAnual = 1.0 / amortFinal;
        double depreciacion = bien.getValorCompra() * porcentajeAnual * añosAjustados;
        double valor = bien.getValorCompra() - depreciacion;

        return aplicarValorResidual(bien.getValorCompra(), valor);
    }

    @Override
    public Aseguradora getAseguradora() {
        return Aseguradora.MAPFRE;
    }
}
