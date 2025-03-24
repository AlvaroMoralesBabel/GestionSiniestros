package com.babelhelloworld.gestionSiniestros.service.strategy;

import java.time.Period;

public class GeneralStrategy extends BaseStrategy {
    @Override
    public double calcularValorReal(Bien bien, double añosUso) {
        
        // Podríamos tener todo este flujo y cada método parametrizado en la BaseStrategy
         
         // Obtener años de uso (si en vez de decimales redondean atributo usaAñosProporcionales)

         // Comprobar si la aseguradora tiene en cuenta o no el año 0

         // Ajustar el periodo de amortización (si tienen en cuenta el doble de tiempo)

         // Calcular la depreciación (teniendo en cuenta si es acumulada o no)

         // Comprobar que el valor final no sea menor que el valor residual
    }
}
