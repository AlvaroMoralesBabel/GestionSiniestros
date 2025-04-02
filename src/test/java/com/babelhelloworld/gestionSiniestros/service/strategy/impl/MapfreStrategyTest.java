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
class MapfreStrategyTest {

    @Mock
    private AmortizacionService mapfreAmortizacionService;

    private MapfreStrategy mapfreStrategy;

    @BeforeEach
    void setUp() {
        mapfreStrategy = new MapfreStrategy(mapfreAmortizacionService);
    }

    @ParameterizedTest(name = "[{index}] {0} => valor={1}, aniosMock={2}, feCompra={3}, feSiniestro={4}, esperado={5}")
    @CsvSource({
            "AUTOMOVIL, 1500, 5, 2025-01-01, 2027-01-01, 900.0",
            "ELECTRODOMESTICO, 1000, 8, 2030-01-01, 2031-01-01, 875.0",
            "INFORMATICA, 1200, 4, 2025-01-01, 2026-07-01, 900.0"
    })
    @DisplayName("MapfreStrategy: casos paramétricos con Mock de MapfreAmortizacionService")
    void calcularValorReal_Parametrizado(
            TipoBien tipoBien,
            double valorCompra,
            int aniosAmortMock,
            String fechaCompra,
            String fechaSiniestro,
            double esperado
    ) {
        // Arrange
        Bien bien = new Bien("BienMapfre", tipoBien, valorCompra, LocalDate.parse(fechaCompra));
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.MAPFRE);
        siniestro.setFechaSiniestro(LocalDate.parse(fechaSiniestro));
        siniestro.setBienesAfectados(List.of(bien));

        when(mapfreAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(aniosAmortMock);

        // Act
        Map<Bien, Double> resultado = mapfreStrategy.calcularValorReal(siniestro);
        double valorCalculado = resultado.get(bien);

        // Assert
        assertEquals(esperado, valorCalculado, 0.0001);
    }

    @ParameterizedTest(name = "Caso {index} => lanza excepción si {1} < {2}")
    @CsvSource({
            "INFORMATICA, 2025-01-01, 2026-01-01",
            "AUTOMOVIL, 2030-06-01, 2031-01-01"
    })
    @DisplayName("MapfreStrategy: lanza excepción si la fecha del siniestro es anterior a la compra")
    void calcularValorReal_FechaInvalida(TipoBien tipoBien, String fechaSin, String fechaCompra) {
        //Arrange
        Bien bien = new Bien("BienError", tipoBien, 1000.0, LocalDate.parse(fechaCompra));
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.MAPFRE);
        siniestro.setFechaSiniestro(LocalDate.parse(fechaSin));
        siniestro.setBienesAfectados(List.of(bien));

        when(mapfreAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(5);

        //Act & Assert
        assertThrows(FechaInvalidaException.class, () -> {
            mapfreStrategy.calcularValorReal(siniestro);
        });
    }
}
