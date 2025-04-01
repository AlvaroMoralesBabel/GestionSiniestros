package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutuaStrategyTest {

    private MutuaStrategy mutuaStrategy;

    @BeforeEach
    void setUp() {
        mutuaStrategy = new MutuaStrategy(Aseguradora.MUTUA);
    }

    @Test
    void calcularValorReal_sinUso_cuentaPrimerAño() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Portátil", 1000, 5, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2030, 1, 1));

        // Act
        double valorCalculado = mutuaStrategy.calcularValorReal(bien, siniestro);

        // Assert
        assertEquals(900.0, valorCalculado, 0.0001);
    }

    @Test
    void calcularValorReal_usoCompletoAmortizacion_devuelveResidual() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Móvil", 1000, 1, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2033, 12, 1));

        // Act
        double valorCalculado = mutuaStrategy.calcularValorReal(bien, siniestro);

        // Assert
        assertEquals(100.0, valorCalculado, 0.0001);
    }

    @Test
    void calcularValorReal_conAniosProporcionales_uso1_5_devuelveCalculoParcial() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Tablet", 2000, 4, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2026, 7, 1));

        // Act
        double valorCalculado = mutuaStrategy.calcularValorReal(bien, siniestro);

        // Assert
        assertTrue(valorCalculado > 1700 && valorCalculado < 1800);
    }

    @Test
    void calcularValorReal_variosAniosUso_caePorDebajoResidual_devuelveResidual() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Coche", 1500, 2, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2035, 7, 1));

        // Act
        double valorCalculado = mutuaStrategy.calcularValorReal(bien, siniestro);

        // Assert
        assertEquals(150.0, valorCalculado, 0.0001);
    }

    @Test
    void calcularValorReal_usoIntermedio_noSeReduceAResidual() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Bicicleta", 1000, 5, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2027, 1, 1));

        // Act
        double valorCalculado = mutuaStrategy.calcularValorReal(bien, siniestro);

        // Assert
        assertEquals(810.0, valorCalculado, 0.0001);
    }
}
