package com.babelhelloworld.gestionSiniestros.service.amortizacion.impl;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import org.springframework.stereotype.Service;

@Service("mapfreAmortizacionService")
public class MapfreAmortizacionService implements AmortizacionService {

    @Override
    public int obtenerAniosAmortizacion(Bien bien) {
        switch (bien.getTipo()) {
            case AUTOMOVIL:
                return 6;
            case ELECTRODOMESTICO:
                return 8;
            case INFORMATICA:
                return 4;
            case MOBILIARIO:
                return 10;
            default:
                return 5;
        }
    }
}
