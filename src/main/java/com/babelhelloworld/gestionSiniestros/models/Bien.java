package com.babelhelloworld.gestionSiniestros.models;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Bien {

    private String nombre;
    private TipoBien tipo;
    private double valorCompra;
    private LocalDate fechaCompra;

    public Bien(String nombre, TipoBien tipo, double valorCompra, LocalDate fechaCompra) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valorCompra = valorCompra;
        this.fechaCompra = fechaCompra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoBien getTipo() {
        return tipo;
    }

    public void setTipo(TipoBien tipo) {
        this.tipo = tipo;
    }

    public double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}
