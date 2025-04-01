package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneralStrategyTest {

    private GeneralStrategy generalStrategy;

    @BeforeEach
    void setUp() {
        generalStrategy = new GeneralStrategy(Aseguradora.GENERAL);
    }

    @Test
    void calcularValorReal_sinUso_devuelveValorOriginal() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Portátil", 1000, 5, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2030, 1, 1));
        // Act
        double valor = generalStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertEquals(1000.0, valor, 0.0001);
    }

    @Test
    void calcularValorReal_usoParcial_aplicaDepreciacion() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Tablet", 2000, 4, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2027, 1, 1));
        // Act
        double valor = generalStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertTrue(valor < 2000 && valor > 1000);
    }

    @Test
    void calcularValorReal_usoCompleto_devuelveResidual() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Coche", 1500, 2, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2030, 1, 1));
        // Act
        double valor = generalStrategy.calcularValorReal(bien, siniestro);
        // Assert
        assertEquals(225.0, valor, 0.0001);
    }
}
