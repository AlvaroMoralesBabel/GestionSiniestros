package com.babelhelloworld.gestionSiniestros.service.strategy;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

import java.util.Map;

public interface ValoracionStrategy {

    Aseguradora getAseguradora();

    Map<Bien, Double> calcularValorReal(Siniestro siniestro);
}
