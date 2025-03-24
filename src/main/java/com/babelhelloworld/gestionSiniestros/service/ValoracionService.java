package com.babelhelloworld.gestionSiniestros.service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;

public interface ValoracionService {

    double calcular(Bien bien, double añosUso, Aseguradora aseguradora);
}
