package com.babelhelloworld.gestionSiniestros.service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import com.babelhelloworld.gestionSiniestros.service.valoracion.ValoracionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ValoracionServiceImplTest {

    private ValoracionStrategy generalStrategy;
    private ValoracionStrategy mapfreStrategy;
    private ValoracionStrategy allianzStrategy;
    private ValoracionStrategy mutuaStrategy;
    private ValoracionServiceImpl valoracionService;

    @BeforeEach
    void setUp() {
        generalStrategy = Mockito.mock(ValoracionStrategy.class);
        mapfreStrategy = Mockito.mock(ValoracionStrategy.class);
        allianzStrategy = Mockito.mock(ValoracionStrategy.class);
        mutuaStrategy = Mockito.mock(ValoracionStrategy.class);

        Map<Aseguradora, ValoracionStrategy> strategyMap = new EnumMap<>(Aseguradora.class);
        strategyMap.put(Aseguradora.GENERAL, generalStrategy);
        strategyMap.put(Aseguradora.MAPFRE, mapfreStrategy);
        strategyMap.put(Aseguradora.ALLIANZ, allianzStrategy);
        strategyMap.put(Aseguradora.MUTUA, mutuaStrategy);

        valoracionService = new ValoracionServiceImpl(strategyMap);
    }

    @Test
    void calcular_conMapfreUsaMapfreStrategy() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Tablet", 2000, 4, LocalDate.of(2025, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2026, 6, 1));

        when(mapfreStrategy.calcularValorReal(any(Bien.class), any(Siniestro.class))).thenReturn(123.45);

        // Act
        double resultado = valoracionService.calcular(siniestro, Aseguradora.MAPFRE);

        // Assert
        verify(mapfreStrategy, times(1)).calcularValorReal(eq(bien), eq(siniestro));
        verifyNoInteractions(generalStrategy, allianzStrategy, mutuaStrategy);
        assertEquals(123.45, resultado, 0.0001);
    }

    @Test
    void calcular_conAllianzUsaAllianzStrategy() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("Móvil", 1000, 2, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2031, 1, 1));

        when(allianzStrategy.calcularValorReal(any(Bien.class), any(Siniestro.class))).thenReturn(200.0);

        // Act
        double resultado = valoracionService.calcular(siniestro, Aseguradora.ALLIANZ);

        // Assert
        verify(allianzStrategy, times(1)).calcularValorReal(eq(bien), eq(siniestro));
        verifyNoInteractions(generalStrategy, mapfreStrategy, mutuaStrategy);
        assertEquals(200.0, resultado, 0.0001);
    }

    @Test
    void calcular_conAseguradoraInexistente_usaGeneralStrategy() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        Bien bien = new Bien("PC", 1500, 3, LocalDate.of(2030, 1, 1));
        siniestro.setBienAfectado(bien);
        siniestro.setFechaSiniestro(LocalDate.of(2032, 1, 1));

        when(generalStrategy.calcularValorReal(any(Bien.class), any(Siniestro.class))).thenReturn(999.9);

        // Act
        double resultado = valoracionService.calcular(siniestro, null);

        // Assert
        verify(generalStrategy, times(1)).calcularValorReal(eq(bien), eq(siniestro));
        verifyNoInteractions(mapfreStrategy, allianzStrategy, mutuaStrategy);
        assertEquals(999.9, resultado, 0.0001);
    }
}
