package com.babelhelloworld.gestionSiniestros.service.amortizacion;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;

public interface AmortizacionService {
    public int obtenerAniosAmortizacion(Aseguradora aseguradora, TipoBien tipoBien);
}
