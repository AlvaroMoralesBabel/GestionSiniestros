package com.babelhelloworld.gestionSiniestros.informe;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

import java.io.IOException;
import java.util.Map;

public interface InformeService {
    void printToConsole(Siniestro siniestro, Map<Bien, Double> valorIndividual);

    String printToFile(Siniestro siniestro, Map<Bien, Double> valorIndividual) throws IOException;
}