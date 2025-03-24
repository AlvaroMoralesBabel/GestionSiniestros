package com.babelhelloworld.gestionSiniestros.service.strategy;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;

public interface ValoracionStrategy {
    double calcularValorReal(Bien bien, double añosUso);

    Aseguradora getAseguradora();
}
