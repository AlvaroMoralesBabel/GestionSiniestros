package com.babelhelloworld.gestionSiniestros.service.siniestro;

import com.babelhelloworld.gestionSiniestros.exceptions.SiniestroSinBienesException;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.valoracion.ValoracionService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GestionSiniestrosServiceImpl implements GestionSiniestrosService {

    private final ValoracionService valoracionService;

    public GestionSiniestrosServiceImpl(ValoracionService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @Override
    public Map<Bien, Double> procesarSiniestro(Siniestro siniestro) {

        Map<Bien, Double> resultado = new HashMap<>();
        List<Bien> bienes = siniestro.getBienesAfectados();
        if (bienes == null || bienes.isEmpty()) {
            throw new SiniestroSinBienesException("El siniestro no tiene bienes afectados");
        }
        for (int i = 0; i < bienes.size(); i++) {
            double valor = valoracionService.calcular(siniestro, i);
            resultado.put(bienes.get(i), valor);
        }
        return resultado;
    }
}
