package com.babelhelloworld.gestionSiniestros.service.valoracion;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    private final Map<Aseguradora, ValoracionStrategy> strategyMap;

    public ValoracionServiceImpl(List<ValoracionStrategy> strategys) {
        this.strategyMap = strategys.stream().collect(toMap(ValoracionStrategy::getAseguradora, Function.identity()));
    }

    @Override
    public Map<Bien, Double> calcular(Siniestro siniestro) {
        ValoracionStrategy strategy = strategyMap.getOrDefault(siniestro.getAseguradora(), strategyMap.get(Aseguradora.GENERAL));
        return strategy.calcularValorReal(siniestro);
    }
}
