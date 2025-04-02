package com.babelhelloworld.gestionSiniestros.service.strategy;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

public interface ValoracionStrategy {

    Aseguradora getAseguradora();

    double calcularValorReal(Siniestro siniestro, int i);
}
