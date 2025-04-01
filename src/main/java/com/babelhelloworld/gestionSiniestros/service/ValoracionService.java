package com.babelhelloworld.gestionSiniestros.service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

public interface ValoracionService {

    double calcular(Siniestro siniestro, Aseguradora aseguradora);
}
