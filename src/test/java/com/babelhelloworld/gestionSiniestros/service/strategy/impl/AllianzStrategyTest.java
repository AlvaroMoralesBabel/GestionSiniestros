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
import org.junit.jupiter.params.provider.CsvFileSource;
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
class AllianzStrategyTest {

    @Mock
    private AmortizacionService allianzAmortizacionService;

    private AllianzStrategy allianzStrategy;

    @BeforeEach
    void setUp() {
        allianzStrategy = new AllianzStrategy(allianzAmortizacionService);
    }

    @ParameterizedTest(name = "[{index}] {0} => valor={1}, aniosMock={2}, feCompra={3}, feSin={4}, esperado={5}")
    @CsvFileSource(resources = "/estrategias/allianz/normal_cases_test.csv", numLinesToSkip = 1)
    @DisplayName("AllianzStrategy: varios escenarios (normal, residual...)")
    void calcularValorReal_EscenariosParametrizados(
            TipoBien tipoBien,
            double valorCompra,
            int aniosAmortMock,
            String fechaCompra,
            String fechaSiniestro,
            double esperado
    ) {
        Bien bien = new Bien("BienTest", tipoBien, valorCompra, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSiniestro, "A_REAL");

        when(allianzAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(aniosAmortMock);

        double resultado = allianzStrategy.calcularValorReal(siniestro, 0);

        assertEquals(esperado, resultado, 0.0001, "Debería calcular correctamente el valor real esperado");
    }

    @ParameterizedTest(name = "[{index}] Falla si {1} < {2}")
    @CsvSource({
            "AUTOMOVIL, 2030-01-01, 2031-01-01",
            "ELECTRODOMESTICO, 2025-05-10, 2026-01-01"
    })
    @DisplayName("AllianzStrategy: lanza excepción si fecha siniestro < fecha compra")
    void calcularValorReal_FechaInvalida(TipoBien tipoBien, String fechaSin, String fechaCompra) {
        Bien bien = new Bien("BienTest", tipoBien, 1000.0, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSin, "A_REAL");

        assertThrows(FechaInvalidaException.class, () -> allianzStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si fecha siniestro < fecha compra");
    }

    @DisplayName("AllianzStrategy: lanza excepción si siniestro no tiene bienes")
    @ParameterizedTest
    @CsvSource({
            "A_REAL",
            "A_NUEVO"
    })
    void calcularValorReal_SiniestroSinBienes(String tipoIndemnizacion) {
        Siniestro siniestro = crearSiniestroOK(List.of(), "2030-01-01", tipoIndemnizacion);
        assertThrows(SiniestroSinBienesException.class, () -> allianzStrategy.calcularValorReal(siniestro, 1),
                "Debería lanzar excepción si siniestro no tiene bienes");
    }

    @DisplayName("AllianzStrategy: lanza excepción si un bien tiene valorCompra < 0")
    @ParameterizedTest
    @CsvSource({
            "AUTOMOVIL, -500",
            "INFORMATICA, -1"
    })
    void calcularValorReal_ValorCompraNegativo(TipoBien tipoBien, double valorNegativo) {
        Bien bien = new Bien("BienNeg", tipoBien, valorNegativo, LocalDate.parse("2025-01-01"));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2030-01-01", "A_NUEVO");

        assertThrows(ValorCompraNegativoException.class, () -> allianzStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si valor de compra < 0");
    }

    @DisplayName("AllianzStrategy: lanza excepción si tipo indemnización es inválido")
    @ParameterizedTest
    @CsvSource({
            "A_NORMAL",
            "TOTAL",
            "A_NULL"
    })
    void calcularValorReal_TipoIndemnizacionInvalida(String tipoInvalido) {
        Bien bien = new Bien("BienTest", TipoBien.AUTOMOVIL, 1000.0, LocalDate.of(2025, 1, 1));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2030-01-01", tipoInvalido);

        assertThrows(TipoIndemnizacionInvalidoException.class, () -> allianzStrategy.calcularValorReal(siniestro, 1),
                "Debería lanzar excepción si tipo indemnización no es A_REAL/A_NUEVO");
    }

    private Siniestro crearSiniestroOK(List<Bien> bienes, String fechaSiniestro, String tipoIndemnizacion) {
        Siniestro sin = new Siniestro();
        sin.setAseguradora(Aseguradora.ALLIANZ);
        sin.setFechaSiniestro(LocalDate.parse(fechaSiniestro));
        sin.setBienesAfectados(bienes);
        sin.setTipoIndemnizacion(tipoIndemnizacion);
        return sin;
    }
}
