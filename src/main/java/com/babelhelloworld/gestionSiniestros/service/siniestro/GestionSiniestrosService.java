package com.babelhelloworld.gestionSiniestros.service.siniestro;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

import java.util.Map;

public interface GestionSiniestrosService {
    Map<Bien, Double> procesarSiniestro(Siniestro siniestro);
}
