package com.babelhelloworld.gestionSiniestros.service;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import com.babelhelloworld.gestionSiniestros.service.valoracion.ValoracionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
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

        when(generalStrategy.getAseguradora()).thenReturn(Aseguradora.GENERAL);
        when(mapfreStrategy.getAseguradora()).thenReturn(Aseguradora.MAPFRE);
        when(allianzStrategy.getAseguradora()).thenReturn(Aseguradora.ALLIANZ);
        when(mutuaStrategy.getAseguradora()).thenReturn(Aseguradora.MUTUA);

        List<ValoracionStrategy> strategyList = List.of(
                generalStrategy, mapfreStrategy, allianzStrategy, mutuaStrategy
        );

        valoracionService = new ValoracionServiceImpl(strategyList);
    }

    @Test
    void calcular_conMapfreUsaMapfreStrategy() {
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.MAPFRE);
        Bien bien = new Bien("Tablet", TipoBien.INFORMATICA, 2000, LocalDate.of(2025, 1, 1));
        siniestro.setBienesAfectados(List.of(bien));
        siniestro.setFechaSiniestro(LocalDate.of(2026, 6, 1));

        Map<Bien, Double> mapaMock = Map.of(bien, 123.45);
        when(mapfreStrategy.calcularValorReal(eq(siniestro))).thenReturn(mapaMock);

        Map<Bien, Double> resultado = valoracionService.calcular(siniestro);

        verify(mapfreStrategy, times(1)).calcularValorReal(eq(siniestro));

        assertEquals(123.45, resultado.get(bien), 0.0001);
        assertEquals(1, resultado.size());
    }

    @Test
    void calcular_conAllianzUsaAllianzStrategy() {
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.ALLIANZ);
        Bien bien = new Bien("Móvil", TipoBien.INFORMATICA, 1000, LocalDate.of(2030, 1, 1));
        siniestro.setBienesAfectados(List.of(bien));
        siniestro.setFechaSiniestro(LocalDate.of(2031, 1, 1));

        Map<Bien, Double> mapaMock = Map.of(bien, 200.0);
        when(allianzStrategy.calcularValorReal(eq(siniestro))).thenReturn(mapaMock);

        Map<Bien, Double> resultado = valoracionService.calcular(siniestro);

        verify(allianzStrategy, times(1)).calcularValorReal(eq(siniestro));

        assertEquals(200.0, resultado.get(bien), 0.0001);
        assertEquals(1, resultado.size());
    }

    @Test
    void calcular_conAseguradoraInexistente_usaGeneralStrategy() {
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(null); // Aseguradora inexistente
        Bien bien = new Bien("PC", TipoBien.INFORMATICA, 1500, LocalDate.of(2030, 1, 1));
        siniestro.setBienesAfectados(List.of(bien));
        siniestro.setFechaSiniestro(LocalDate.of(2032, 1, 1));

        Map<Bien, Double> mapaMock = Map.of(bien, 999.9);
        when(generalStrategy.calcularValorReal(eq(siniestro))).thenReturn(mapaMock);

        Map<Bien, Double> resultado = valoracionService.calcular(siniestro);

        verify(generalStrategy, times(1)).calcularValorReal(eq(siniestro));

        assertEquals(999.9, resultado.get(bien), 0.0001);
        assertEquals(1, resultado.size());
    }
}
