package com.babelhelloworld.gestionSiniestros.service.strategy;

public abstract class BaseStrategy implements ValoracionStrategy{

    protected Aseguradora aseguradora;

    public BaseStrategy(Aseguradora aseguradora) {
        this.aseguradora = aseguradora;
    }

    @Override
    public abstract double calcularValorReal(Bien bien, double aniosUso);

    protected double aplicarValorResidual(double valorCompra, double valorCalculado) {
        if (!aseguradora.isAplicaValorResidual()) return valorCalculado;
        double residual = valorCompra * aseguradora.getValorResidual();
        return Math.max(residual, valorCalculado);
    }

}