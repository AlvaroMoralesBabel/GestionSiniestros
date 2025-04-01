package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AllianzStrategyTest {

    private AllianzStrategy allianzStrategy;

    @BeforeEach
    void setUp() {
        allianzStrategy = new AllianzStrategy(Aseguradora.ALLIANZ);
    }

    @Test
    void calcularValorReal_sinUso_devuelveValorOriginalMasAumento() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Portátil", 1000, 5, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2030, 1, 1));
        // Act
        double valor = allianzStrategy.calcularValorReal(bien, siniestro);
        // Assert
        // Allianz sin depreciación, +5% => 1000 * 1.05
        assertEquals(1050.0, valor, 0.0001);
    }

    @Test
    void calcularValorReal_usoParcial_aplicaDepreciacionYAumento() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Tablet", 2000, 4, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2026, 7, 1));
        // Act
        double valor = allianzStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertTrue(valor > 1000 && valor < 2000);
    }

    @Test
    void calcularValorReal_usoCompleto_devuelveMinimoYAumento() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Coche", 1500, 4, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2035, 1, 1));
        // Act
        double valor = allianzStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertTrue(valor > 300 && valor < 400);
    }
}
