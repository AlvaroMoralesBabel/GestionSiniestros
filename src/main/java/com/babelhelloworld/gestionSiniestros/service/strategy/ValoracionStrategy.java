package com.babelhelloworld.gestionSiniestros.service.strategy;

import com.babelhelloworld.gestionSiniestros.models.Siniestro;

public interface ValoracionStrategy {
    double calcularValorReal(Siniestro siniestro);
}
