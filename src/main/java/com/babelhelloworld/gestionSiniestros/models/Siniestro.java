package com.babelhelloworld.gestionSiniestros.models;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class Siniestro {

    private LocalDate fechaSiniestro;
    private Bien bienAfectado;
    private String tipoIndemnizacion;
    private String compania;

    public LocalDate getFechaSiniestro() { 
        return fechaSiniestro; 
        }
    public void setFechaSiniestro(LocalDate fechaSiniestro) { 
        this.fechaSiniestro = fechaSiniestro; }
    public Bien getBienAfectado() { 
        return bienAfectado; 
        }
    public void setBienAfectado(Bien bienAfectado) { 
        this.bienAfectado = bienAfectado; 
        }
    public String getTipoIndemnizacion() { 
        return tipoIndemnizacion; 
        }
    public void setTipoIndemnizacion(String tipoIndemnizacion) { 
        this.tipoIndemnizacion = tipoIndemnizacion; }

    public String getCompania() { 
        return compania; 
        }

    public void setCompania(String compania) { 
        this.compania = compania; 
        }
}
