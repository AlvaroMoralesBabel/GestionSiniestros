package com.babelhelloworld.gestionSiniestros.service;

import org.springframework.stereotype.Service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.AllianzStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.GeneralStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.MapfreStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.MutuaStrategy;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    @Override
    public double calcular(Bien bien, double añosUso, Aseguradora aseguradora){
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
