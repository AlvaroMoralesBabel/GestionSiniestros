package com.babelhelloworld.gestionSiniestros.service.valoracion;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    private final Map<Aseguradora, ValoracionStrategy> strategyMap;

    public ValoracionServiceImpl(Map<Aseguradora, ValoracionStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    @Override
    public double calcular(Siniestro siniestro) {
        ValoracionStrategy strategy = strategyMap.getOrDefault(siniestro.getAseguradora(), strategyMap.get(Aseguradora.GENERAL));
        return strategy.calcularValorReal(siniestro);
    }
}
