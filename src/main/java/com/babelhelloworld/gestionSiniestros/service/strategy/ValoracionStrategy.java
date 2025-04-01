package com.babelhelloworld.gestionSiniestros.service.strategy;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

public interface ValoracionStrategy {
    double calcularValorReal(Bien bien, Siniestro siniestro);
}
