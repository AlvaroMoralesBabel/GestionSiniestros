package com.babelhelloworld.gestionSiniestros.models;

import org.springframework.stereotype.Component;

public class Bien {
    private String tipo;
    private double valorCompra;
    private Integer añosAmortizacion;

    public Bien(String tipo, double valorCompra, Integer añosAmortizacion){
        this.tipo = tipo;
        this.valorCompra = valorCompra;
        this.añosAmortizacion = añosAmortizacion;
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
    
    public Integer getAñosAmortizacion() { 
        return añosAmortizacion; 
    }

    public void setAñosAmortizacion(Integer añosAmortizacion) { 
        this.añosAmortizacion = añosAmortizacion; 
    }
}
