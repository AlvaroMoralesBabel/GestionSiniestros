package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.exceptions.FechaInvalidaException;
import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import com.babelhelloworld.gestionSiniestros.service.amortizacion.AmortizacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AllianzStrategyTest {

    @Mock
    private AmortizacionService allianzAmortizacionService;

    private AllianzStrategy allianzStrategy;

    @BeforeEach
    void setUp() {
        allianzStrategy = new AllianzStrategy(allianzAmortizacionService);
    }

    @ParameterizedTest(name = "[{index}] {0} => valor={1}, aniosMock={2}, feCompra={3}, feSin={4}, esperado={5}")
    @CsvSource({
            "AUTOMOVIL, 2000, 4, 2030-01-01, 2032-01-01, 1181.25",
            "ELECTRODOMESTICO, 1000, 6, 2030-01-01, 2030-01-01, 1050.0",
            "INFORMATICA, 1500, 4, 2025-01-01, 2030-01-01, 373.7548828125"
    })
    @DisplayName("AllianzStrategy: varios escenarios (normal, residual...)")
    void calcularValorReal_EscenariosParametrizados(
            TipoBien tipoBien,
            double valorCompra,
            int aniosAmortMock,
            String fechaCompra,
            String fechaSiniestro,
            double esperado
    ) {
        // Arrange
        Bien bien = new Bien("BienTest", tipoBien, valorCompra, LocalDate.parse(fechaCompra));
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.ALLIANZ);
        siniestro.setFechaSiniestro(LocalDate.parse(fechaSiniestro));
        siniestro.setBienesAfectados(List.of(bien));

        when(allianzAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(aniosAmortMock);

        // Act
        Map<Bien, Double> resultado = allianzStrategy.calcularValorReal(siniestro);
        double valorCalculado = resultado.get(bien);

        // Assert
        assertEquals(esperado, valorCalculado, 0.0001);
    }

    @ParameterizedTest(name = "[{index}] Falla si {1} < {2}")
    @CsvSource({
            "AUTOMOVIL, 2030-01-01, 2031-01-01",
            "ELECTRODOMESTICO, 2025-05-10, 2026-01-01"
    })
    @DisplayName("AllianzStrategy: lanza excepción si fecha siniestro < fecha compra")
    void calcularValorReal_FechaInvalida(TipoBien tipoBien, String fechaSin, String fechaCompra) {
        // Arrange
        Bien bien = new Bien("BienTest", tipoBien, 1000.0, LocalDate.parse(fechaCompra));
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.ALLIANZ);
        siniestro.setFechaSiniestro(LocalDate.parse(fechaSin));
        siniestro.setBienesAfectados(List.of(bien));

        when(allianzAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(5);

        // Act & Assert
        assertThrows(FechaInvalidaException.class, () -> {
            allianzStrategy.calcularValorReal(siniestro);
        });
    }
}
