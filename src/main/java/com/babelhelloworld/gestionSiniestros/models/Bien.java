package com.babelhelloworld.gestionSiniestros.models;

public class Bien {
    private String tipo;
    private LocalDate fechaCompra;
    private double valorCompra;

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
        this.valorCompra = valorCompra; }

    public LocalDate getFechaCompra() { 
        return fechaCompra; 
    }

    public void setFechaCompra(LocalDate fechaCompra) { 
        this.fechaCompra = fechaCompra; 
    }
}
