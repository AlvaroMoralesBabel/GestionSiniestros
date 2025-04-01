package com.babelhelloworld.gestionSiniestros.models;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Bien {

    private String tipo;
    private double valorCompra;
    private Integer aniosAmortizacion;
    private LocalDate fechaCompra;

    public Bien(String tipo, double valorCompra, Integer aniosAmortizacion, LocalDate fechaCompra) {
        this.tipo = tipo;
        this.valorCompra = valorCompra;
        this.aniosAmortizacion = aniosAmortizacion;
        this.fechaCompra = fechaCompra;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public Integer getAniosAmortizacion() {
        return aniosAmortizacion;
    }

    public void setAniosAmortizacion(Integer aniosAmortizacion) {
        this.aniosAmortizacion = aniosAmortizacion;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}
