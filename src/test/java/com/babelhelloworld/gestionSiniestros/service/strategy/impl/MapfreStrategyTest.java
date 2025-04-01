package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapfreStrategyTest {

    private MapfreStrategy mapfreStrategy;

    @BeforeEach
    void setUp() {
        mapfreStrategy = new MapfreStrategy(Aseguradora.MAPFRE);
    }

    @Test
    void calcularValorReal_sinUso_devuelveValorOriginal() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Portátil", 1000, 5, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2030, 1, 1));
        // Act
        double valor = mapfreStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertEquals(1000.0, valor, 0.0001);
    }

    @Test
    void calcularValorReal_usoParcial_aplicaDepreciacionNoAcumulada() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Tablet", 2000, 4, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2026, 7, 1));
        // Act
        double valor = mapfreStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertEquals(1500.0, valor, 0.0001);
    }

    @Test
    void calcularValorReal_usoCompleto_devuelveResidual() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Móvil", 1000, 1, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2032, 1, 1));
        // Act
        double valor = mapfreStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertEquals(170.0, valor, 0.0001);
    }
}
