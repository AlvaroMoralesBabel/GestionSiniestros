package com.babelhelloworld.gestionSiniestros.service.amortizacion.impl;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import org.springframework.stereotype.Service;

@Service("allianzAmortizacionService")
public class AllianzAmortizacionService implements AmortizacionService {

    @Override
    public int obtenerAniosAmortizacion(Bien bien) {
        switch (bien.getTipo()) {
            case AUTOMOVIL:
                return 4;
            case ELECTRODOMESTICO:
                return 6;
            case INFORMATICA:
                return 4;
            case MOBILIARIO:
                return 10;
            default:
                return 5;
        }
    }
}
