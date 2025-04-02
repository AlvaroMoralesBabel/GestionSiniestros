package com.babelhelloworld.gestionSiniestros.service.strategy;

import com.babelhelloworld.gestionSiniestros.exceptions.*;
import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class BaseStrategy implements ValoracionStrategy {

    @Override
    public abstract double calcularValorReal(Siniestro siniestro, int i);

    @Override
    public abstract Aseguradora getAseguradora();

    /**
     * Valida que el siniestro y sus bienes cumplan las reglas de negocio.
     */
    protected void validarSiniestro(Siniestro siniestro) {
        if (siniestro == null) {
            throw new SiniestroInvalidoException("Siniestro nulo.");
        }
        if (siniestro.getBienesAfectados() == null || siniestro.getBienesAfectados().isEmpty()) {
            throw new SiniestroSinBienesException("El siniestro no tiene ningún bien afectado.");
        }
        if (siniestro.getAseguradora() == null) {
            throw new AseguradoraInvalidaException("Aseguradora nula o desconocida.");
        }
        if (!"A_REAL".equalsIgnoreCase(siniestro.getTipoIndemnizacion())
                && !"A_NUEVO".equalsIgnoreCase(siniestro.getTipoIndemnizacion())) {
            throw new TipoIndemnizacionInvalidoException(
                    "Tipo de indemnización no válida: " + siniestro.getTipoIndemnizacion()
            );
        }
    }

    /**
     * Valida reglas específicas de cada bien.
     */
    protected void validarBien(Bien bien, Siniestro siniestro) {
        if (bien.getValorCompra() < 0) {
            throw new ValorCompraNegativoException("Valor de compra negativo para el bien: " + bien.getNombre());
        }
        LocalDate fechaCompra = bien.getFechaCompra();
        LocalDate fechaSin = siniestro.getFechaSiniestro();
        if (fechaCompra != null && fechaSin != null && fechaCompra.isAfter(fechaSin)) {
            throw new FechaInvalidaException(
                    "La fecha de siniestro (" + fechaSin
                            + ") es anterior a la fecha de compra (" + fechaCompra + ") del bien: "
                            + bien.getNombre()
            );
        }
        if (bien.getTipo() == null) {
            throw new BienInvalidoException("Tipo de bien no definido en: " + bien.getNombre());
        }
    }

    /**
     * Devuelve la diferencia en años (posiblemente con decimales)
     * entre la fechaCompra del bien y la fechaSiniestro.
     */
    protected double getAniosTranscurridos(Bien bien, Siniestro siniestro) {
        if (bien.getFechaCompra() == null || siniestro.getFechaSiniestro() == null) {
            return 0.0;
        }
        long daysBetween = ChronoUnit.DAYS.between(
                bien.getFechaCompra(),
                siniestro.getFechaSiniestro()
        );
        return daysBetween / 365.0;
    }

    /**
     * Si la compañía no usa años proporcionales, se trunca a entero.
     */
    protected double calcularAniosUso(double anios, boolean usaAniosProporcionales) {
        return usaAniosProporcionales ? anios : (int) anios;
    }

    /**
     * Si se cuenta el primer año completo, y aniosUso < 1, forzamos a 1.0
     */
    protected double aplicarPrimerAnio(double anios, boolean cuentaPrimerAnio) {
        return (cuentaPrimerAnio && anios < 1) ? 1.0 : anios;
    }

    /**
     * Depreciación acumulada año a año para 'añosEnteros'.
     */
    protected double depreciacionAcumulada(double valorInicial, double porcentajeAnual, int añosEnteros) {
        double valor = valorInicial;
        for (int i = 0; i < añosEnteros; i++) {
            valor -= (valor * porcentajeAnual);
        }
        return valor;
    }

    /**
     * Aplica un multiplicador a los años de amortización (ej. Mutua: *2).
     */
    protected int aplicarMultiplicador(int aniosAmortOriginal, int multiplicador) {
        return aniosAmortOriginal * multiplicador;
    }

    /**
     * Aplica valor residual según el % en la Aseguradora.
     */
    protected double aplicarValorResidual(double valorCompra, double valorCalculado, double valorResidual) {
        if (valorResidual <= 0) {
            return valorCalculado;
        }
        double residual = valorCompra * valorResidual;
        return Math.max(residual, valorCalculado);
    }
}
