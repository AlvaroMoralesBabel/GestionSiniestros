package com.babelhelloworld.gestionSiniestros.service.amortizacion.impl;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import org.springframework.stereotype.Service;

@Service("mutuaAmortizacionService")
public class MutuaAmortizacionService implements AmortizacionService {

    @Override
    public int obtenerAniosAmortizacion(Bien bien) {
        switch (bien.getTipo()) {
            case AUTOMOVIL:
                return 5;
            case ELECTRODOMESTICO:
                return 10;
            case INFORMATICA:
                return 5;
            case MOBILIARIO:
                return 12;
            default:
                return 6;
        }
    }
}
