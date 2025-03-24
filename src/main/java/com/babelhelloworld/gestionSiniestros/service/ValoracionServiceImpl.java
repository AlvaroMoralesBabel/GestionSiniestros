package com.babelhelloworld.gestionSiniestros.service;

public class ValoracionServiceImpl implements ValoracionService {

    @Override
    public double calcular(Bien bien, double añosUso, Aseguradora aseguradora){
        return getStrategy(aseguradora).calcularValorReal(bien, añosUso);
    }

    private ValoracionStrategy getStrategy(Aseguradora aseguradora) {
        return switch (aseguradora) {
            case MAPFRE -> new MapfreStrategy();
            case ALLIANZ -> new AllianzStrategy();
            case MUTUA_MADRILENA -> new MutuaStrategy();
            default -> new GeneralStrategy();
        }
    }
}
