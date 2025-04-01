package com.babelhelloworld.gestionSiniestros.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Siniestro {

    private LocalDate fechaSiniestro;
    private LocalTime horaSiniestro;
    private String descripcion;
    private String numeroPoliza;
    private String direccion;
    private Aseguradora aseguradora;
    private String tipoIndemnizacion; // "A NUEVO" o "A REAL"
    private List<Bien> bienesAfectados;

    public LocalDate getFechaSiniestro() {
        return fechaSiniestro;
    }

    public void setFechaSiniestro(LocalDate fechaSiniestro) {
        this.fechaSiniestro = fechaSiniestro;
    }

    public LocalTime getHoraSiniestro() {
        return horaSiniestro;
    }

    public void setHoraSiniestro(LocalTime horaSiniestro) {
        this.horaSiniestro = horaSiniestro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Aseguradora getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(Aseguradora aseguradora) {
        this.aseguradora = aseguradora;
    }

    public String getTipoIndemnizacion() {
        return tipoIndemnizacion;
    }

    public void setTipoIndemnizacion(String tipoIndemnizacion) {
        this.tipoIndemnizacion = tipoIndemnizacion;
    }

    public List<Bien> getBienesAfectados() {
        return bienesAfectados;
    }

    public void setBienesAfectados(List<Bien> bienesAfectados) {
        this.bienesAfectados = bienesAfectados;
    }
}
