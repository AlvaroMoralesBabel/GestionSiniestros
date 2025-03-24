package com.babelhelloworld.gestionSiniestros.service.strategy;

import org.springframework.stereotype.Service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;

@Service
public abstract class BaseStrategy implements ValoracionStrategy{

    protected Aseguradora aseguradora;

    public BaseStrategy(Aseguradora aseguradora) {
        this.aseguradora = aseguradora;
    }

    @Override
    public abstract double calcularValorReal(Bien bien, double añosUso);

    protected double aplicarValorResidual(double valorCompra, double valorCalculado) {
        if (aseguradora.getValorResidual() == 0) return valorCalculado;
        double residual = valorCompra * aseguradora.getValorResidual();
        return Math.max(residual, valorCalculado);
    }

    protected double calcularAñosUso(double añosUso, boolean usaAñosProporcionales){
        if(!usaAñosProporcionales){
            return (int) añosUso;
        }
        return añosUso;
    }

    protected double aplicarPrimerAño(double añosUso, boolean isCuentaPrimerAño){
        if(isCuentaPrimerAño && añosUso < 1){
            return 1.0;
        }
        return añosUso;
    }

    protected double depreciacionAcumulada(double valorInicial, double porcentajeAnual, int añosEnteros){
        double valor = valorInicial;

        for(int i = 0; i < añosEnteros; i++){
            valor -= valor * porcentajeAnual;
        }

        return valor;
    }

    protected int aplicarMultiplicador(int aniosAmortOriginal, int multiplicador) {
        return aniosAmortOriginal * multiplicador;
    }
}