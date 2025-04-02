package com.babelhelloworld.gestionSiniestros.service.valoracion;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import com.babelhelloworld.gestionSiniestros.service.strategy.ValoracionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

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

        double doubleMock = 123.45;

        when(mapfreStrategy.calcularValorReal(eq(siniestro), eq(1))).thenReturn(doubleMock);

        double resultado = valoracionService.calcular(siniestro, 1);

        verify(mapfreStrategy, times(1)).calcularValorReal(eq(siniestro), eq(1));

        assertEquals(123.45, resultado, 0.0001, "Debería usar MapfreStrategy");
    }

    @Test
    void calcular_conAllianzUsaAllianzStrategy() {
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.ALLIANZ);
        Bien bien = new Bien("Móvil", TipoBien.INFORMATICA, 1000, LocalDate.of(2030, 1, 1));
        siniestro.setBienesAfectados(List.of(bien));
        siniestro.setFechaSiniestro(LocalDate.of(2031, 1, 1));

        double doubleMock = 200.0;

        when(allianzStrategy.calcularValorReal(eq(siniestro), eq(1))).thenReturn(doubleMock);

        double resultado = valoracionService.calcular(siniestro, 1);

        verify(allianzStrategy, times(1)).calcularValorReal(eq(siniestro), eq(1));

        assertEquals(200.0, resultado, 0.0001, "Debería usar AllianzStrategy");
    }

    @Test
    void calcular_conAseguradoraInexistente_usaGeneralStrategy() {
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(null); // Aseguradora inexistente
        Bien bien = new Bien("PC", TipoBien.INFORMATICA, 1500, LocalDate.of(2030, 1, 1));
        siniestro.setBienesAfectados(List.of(bien));
        siniestro.setFechaSiniestro(LocalDate.of(2032, 1, 1));

        double doubleMock = 999.9;

        when(generalStrategy.calcularValorReal(eq(siniestro), eq(1))).thenReturn(doubleMock);

        double resultado = valoracionService.calcular(siniestro, 1);

        verify(generalStrategy, times(1)).calcularValorReal(eq(siniestro), eq(1));

        assertEquals(999.9, resultado, 0.0001, "Debería usar GeneralStrategy");
    }

    @Test
    void calcular_conAseguradoraInexistente_usaMutuaStrategy() {
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.MUTUA);
        Bien bien = new Bien("Móvil", TipoBien.INFORMATICA, 1000, LocalDate.of(2030, 1, 1));
        siniestro.setBienesAfectados(List.of(bien));
        siniestro.setFechaSiniestro(LocalDate.of(2031, 1, 1));

        double doubleMock = 200.0;

        when(mutuaStrategy.calcularValorReal(eq(siniestro), eq(1))).thenReturn(doubleMock);

        double resultado = valoracionService.calcular(siniestro, 1);

        verify(mutuaStrategy, times(1)).calcularValorReal(eq(siniestro), eq(1));

        assertEquals(200.0, resultado, 0.0001, "Debería usar MutuaStrategy");
    }
}
