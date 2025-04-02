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
class GeneralStrategyTest {

    @Mock
    private AmortizacionService generalAmortizacionService;

    private GeneralStrategy generalStrategy;

    @BeforeEach
    void setUp() {
        generalStrategy = new GeneralStrategy(generalAmortizacionService);
    }

    @ParameterizedTest(name = "[{index}] {0} => valor={1}, aniosMock={2}, feCompra={3}, feSiniestro={4}, esperado={5}")
    @CsvSource({
            "INFORMATICA, 1000, 3, 2025-01-01, 2028-01-01, 296.29629629629636",
            "ELECTRODOMESTICO, 2000, 7, 2030-01-01, 2032-01-01, 1469.3877551020407",
            "AUTOMOVIL, 3000, 5, 2025-01-01, 2026-01-01, 2400.0"
    })
    @DisplayName("GeneralStrategy: casos paramétricos con Mock de GeneralAmortizacionService")
    void calcularValorReal_Parametrizado(
            TipoBien tipoBien,
            double valorCompra,
            int aniosAmortMock,
            String fechaCompra,
            String fechaSiniestro,
            double esperado
    ) {
        // Arrange
        Bien bien = new Bien("BienGen", tipoBien, valorCompra, LocalDate.parse(fechaCompra));
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.GENERAL);
        siniestro.setFechaSiniestro(LocalDate.parse(fechaSiniestro));
        siniestro.setBienesAfectados(List.of(bien));

        when(generalAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(aniosAmortMock);

        // Act
        Map<Bien, Double> resultado = generalStrategy.calcularValorReal(siniestro);
        double valorCalculado = resultado.get(bien);

        // Assert
        assertEquals(esperado, valorCalculado, 0.0001);
    }

    @ParameterizedTest(name = "Caso {index} => lanza excepción si {1} < {2}")
    @CsvSource({
            "INFORMATICA, 2027-01-01, 2028-05-01",
            "AUTOMOVIL, 2030-06-01, 2031-10-01"
    })
    @DisplayName("GeneralStrategy: lanza excepción si la fecha del siniestro es anterior a la compra")
    void calcularValorReal_FechaInvalida(TipoBien tipoBien, String fechaSin, String fechaCompra) {
        // Arrange
        Bien bien = new Bien("BienErrorGen", tipoBien, 1000.0, LocalDate.parse(fechaCompra));
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.GENERAL);
        siniestro.setFechaSiniestro(LocalDate.parse(fechaSin));
        siniestro.setBienesAfectados(List.of(bien));

        when(generalAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(5);

        // Act & Assert
        assertThrows(FechaInvalidaException.class, () -> {
            generalStrategy.calcularValorReal(siniestro);
        });
    }
}
