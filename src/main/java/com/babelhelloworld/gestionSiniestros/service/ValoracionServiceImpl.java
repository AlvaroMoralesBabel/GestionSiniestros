package com.babelhelloworld.gestionSiniestros.service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.AllianzStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.GeneralStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.MapfreStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.MutuaStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    private final Map<String, ValoracionStrategy> estrategias;

    @Autowired
    public ValoracionServiceImpl(List<ValoracionStrategy> estrategiasList) {
        this.estrategias = new HashMap<>();
        for (ValoracionStrategy strategy : estrategiasList) {
            estrategias.put(strategy.getAseguradora().name(), strategy);
        }
    }

    @Override
    public double calcular(Bien bien, double añosUso, Aseguradora aseguradora) {
        return getStrategy(aseguradora).calcularValorReal(bien, añosUso);
    }

    private ValoracionStrategy getStrategy(Aseguradora aseguradora) {
        return switch (aseguradora) {
            case MAPFRE -> new MapfreStrategy(aseguradora);
            case ALLIANZ -> new AllianzStrategy(aseguradora);
            case MUTUA -> new MutuaStrategy(aseguradora);
            default -> new GeneralStrategy(aseguradora);
        };
    }
}
