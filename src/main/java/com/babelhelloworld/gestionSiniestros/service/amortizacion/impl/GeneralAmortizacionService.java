package com.babelhelloworld.gestionSiniestros.service.amortizacion.impl;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import org.springframework.stereotype.Service;

@Service("generalAmortizacionService")
public class GeneralAmortizacionService implements AmortizacionService {

    @Override
    public int obtenerAniosAmortizacion(Bien bien) {
        switch (bien.getTipo()) {
            case AUTOMOVIL:
                return 5;
            case ELECTRODOMESTICO:
                return 7;
            case INFORMATICA:
                return 3;
            case MOBILIARIO:
                return 8;
            default:
                return 4;
        }
    }
}
