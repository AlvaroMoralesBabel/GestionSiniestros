package com.babelhelloworld.gestionSiniestros.config;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class AmortizacionConfig {

    @Bean
    public Map<Aseguradora, Map<TipoBien, Integer>> añosAmortizacionPorAseguradora() {
        Map<Aseguradora, Map<TipoBien, Integer>> configuracion = new EnumMap<>(Aseguradora.class);

        configuracion.put(Aseguradora.MAPFRE, Map.of(
                TipoBien.ELECTRODOMESTICO, 8,
                TipoBien.AUTOMOVIL, 6,
                TipoBien.INFORMATICA, 4,
                TipoBien.MOBILIARIO, 10,
                TipoBien.OTRO, 5
        ));

        configuracion.put(Aseguradora.MUTUA, Map.of(
                TipoBien.ELECTRODOMESTICO, 10,
                TipoBien.AUTOMOVIL, 5,
                TipoBien.INFORMATICA, 5,
                TipoBien.MOBILIARIO, 12,
                TipoBien.OTRO, 6
        ));

        configuracion.put(Aseguradora.GENERAL, Map.of(
                TipoBien.ELECTRODOMESTICO, 7,
                TipoBien.AUTOMOVIL, 5,
                TipoBien.INFORMATICA, 3,
                TipoBien.MOBILIARIO, 8,
                TipoBien.OTRO, 4
        ));

        configuracion.put(Aseguradora.ALLIANZ, Map.of(
                TipoBien.ELECTRODOMESTICO, 6,
                TipoBien.AUTOMOVIL, 4,
                TipoBien.INFORMATICA, 4,
                TipoBien.MOBILIARIO, 10,
                TipoBien.OTRO, 5
        ));

        return configuracion;
    }
}
