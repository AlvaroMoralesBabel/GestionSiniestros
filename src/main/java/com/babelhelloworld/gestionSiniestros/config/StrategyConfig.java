package com.babelhelloworld.gestionSiniestros.config;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.AllianzStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.GeneralStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.MapfreStrategy;
import com.babelhelloworld.gestionSiniestros.service.strategy.impl.MutuaStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class StrategyConfig {

    private final AmortizacionService amortizacionService;

    public StrategyConfig(AmortizacionService amortizacionService) {
        this.amortizacionService = amortizacionService;
    }

    @Bean
    public ValoracionStrategy generalStrategy() {
        return new GeneralStrategy(Aseguradora.GENERAL, amortizacionService);
    }

    @Bean
    public ValoracionStrategy mapfreStrategy() {
        return new MapfreStrategy(Aseguradora.MAPFRE, amortizacionService);
    }

    @Bean
    public ValoracionStrategy allianzStrategy() {
        return new AllianzStrategy(Aseguradora.ALLIANZ, amortizacionService);
    }

    @Bean
    public ValoracionStrategy mutuaStrategy() {
        return new MutuaStrategy(Aseguradora.MUTUA, amortizacionService);
    }

    @Bean
    public Map<Aseguradora, ValoracionStrategy> strategyMap(
            ValoracionStrategy generalStrategy,
            ValoracionStrategy mapfreStrategy,
            ValoracionStrategy allianzStrategy,
            ValoracionStrategy mutuaStrategy
    ) {
        Map<Aseguradora, ValoracionStrategy> map = new EnumMap<>(Aseguradora.class);
        map.put(Aseguradora.GENERAL, generalStrategy);
        map.put(Aseguradora.MAPFRE, mapfreStrategy);
        map.put(Aseguradora.ALLIANZ, allianzStrategy);
        map.put(Aseguradora.MUTUA, mutuaStrategy);
        return map;
    }
}
