package com.babelhelloworld.gestionSiniestros.service.valoracion;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

import java.util.Map;

public interface ValoracionService {

    Map<Bien, Double> calcular(Siniestro siniestro);
}
