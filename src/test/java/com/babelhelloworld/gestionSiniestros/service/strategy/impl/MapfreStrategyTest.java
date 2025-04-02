package com.babelhelloworld.gestionSiniestros.service.strategy.impl;

import com.babelhelloworld.gestionSiniestros.exceptions.FechaInvalidaException;
import com.babelhelloworld.gestionSiniestros.exceptions.SiniestroSinBienesException;
import com.babelhelloworld.gestionSiniestros.exceptions.TipoIndemnizacionInvalidoException;
import com.babelhelloworld.gestionSiniestros.exceptions.ValorCompraNegativoException;
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
    @DisplayName("MapfreStrategy: escenarios parametrizados de valor normal/residual")
    void calcularValorReal_Parametrizado(
            TipoBien tipoBien,
            double valorCompra,
            int aniosAmortMock,
            String fechaCompra,
            String fechaSiniestro,
            double esperado
    ) {
        Bien bien = new Bien("BienMapfre", tipoBien, valorCompra, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSiniestro, "A_REAL");

        when(mapfreAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(aniosAmortMock);

        double resultado = mapfreStrategy.calcularValorReal(siniestro, 0);

        assertEquals(esperado, resultado, 0.0001, "Debería devolver el valor esperado");
    }

    @ParameterizedTest(name = "Falla fecha si {1} < {2}")
    @CsvSource({
            "INFORMATICA, 2030-01-01, 2031-05-01",
            "AUTOMOVIL, 2028-12-01, 2029-01-01"
    })
    @DisplayName("MapfreStrategy: lanza FechaInvalidaException si siniestro < compra")
    void calcularValorReal_FechaInvalida(TipoBien tipoBien, String fechaSin, String fechaCompra) {
        Bien bien = new Bien("BienMapfreErr", tipoBien, 1200.0, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSin, "A_REAL");

        assertThrows(FechaInvalidaException.class, () -> mapfreStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si fecha siniestro < fecha compra");
    }

    @DisplayName("MapfreStrategy: lanza SiniestroSinBienesException si lista bienes vacía")
    @ParameterizedTest
    @CsvSource({"A_REAL", "A_NUEVO"})
    void calcularValorReal_SiniestroSinBienes(String tipoIndemnizacion) {
        Siniestro siniestro = crearSiniestroOK(List.of(), "2030-01-01", tipoIndemnizacion);

        assertThrows(SiniestroSinBienesException.class, () -> mapfreStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si siniestro no tiene bienes");
    }

    @DisplayName("MapfreStrategy: lanza ValorCompraNegativoException si valorCompra < 0")
    @ParameterizedTest
    @CsvSource({
            "AUTOMOVIL, -500.0",
            "ELECTRODOMESTICO, -100.5"
    })
    void calcularValorReal_ValorCompraNegativo(TipoBien tipoBien, double valorNegativo) {
        Bien bien = new Bien("BienNeg", tipoBien, valorNegativo, LocalDate.of(2025, 1, 1));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2029-01-01", "A_NUEVO");

        assertThrows(ValorCompraNegativoException.class, () -> mapfreStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si valor de compra < 0");
    }

    @DisplayName("MapfreStrategy: lanza TipoIndemnizacionInvalidaException si indemnización no es A_REAL / A_NUEVO")
    @ParameterizedTest
    @CsvSource({"ROTO", "ABRACADABRA", "X"})
    void calcularValorReal_TipoIndemnizacionInvalida(String tipoInvalido) {
        Bien bien = new Bien("BienMap", TipoBien.AUTOMOVIL, 1000.0, LocalDate.of(2025, 1, 1));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2030-01-01", tipoInvalido);

        assertThrows(TipoIndemnizacionInvalidoException.class, () -> mapfreStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si tipo indemnización no es A_REAL / A_NUEVO");
    }

    private Siniestro crearSiniestroOK(List<Bien> bienes, String fechaSin, String tipoIndemnizacion) {
        Siniestro sin = new Siniestro();
        sin.setAseguradora(Aseguradora.MAPFRE);
        sin.setFechaSiniestro(LocalDate.parse(fechaSin));
        sin.setBienesAfectados(bienes);
        sin.setTipoIndemnizacion(tipoIndemnizacion);
        return sin;
    }
}
