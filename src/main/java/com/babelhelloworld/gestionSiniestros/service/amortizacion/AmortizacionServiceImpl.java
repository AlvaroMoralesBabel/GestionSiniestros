package com.babelhelloworld.gestionSiniestros.service.amortizacion;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class AmortizacionServiceImpl implements AmortizacionService {
    private final Map<Aseguradora, Map<TipoBien, Integer>> amortizaciones;

    public AmortizacionServiceImpl(Map<Aseguradora, Map<TipoBien, Integer>> amortizaciones) {
        this.amortizaciones = amortizaciones;
    }

    public int obtenerAniosAmortizacion(Aseguradora aseguradora, TipoBien tipoBien) {
        return amortizaciones
                .getOrDefault(aseguradora, Collections.emptyMap())
                .getOrDefault(tipoBien, 5); // valor por defecto si no se encuentra
    }
}
