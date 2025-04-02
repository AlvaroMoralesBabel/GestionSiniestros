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
    @DisplayName("GeneralStrategy: escenarios parametrizados (residual, etc.)")
    void calcularValorReal_Parametrizado(
            TipoBien tipoBien,
            double valorCompra,
            int aniosMock,
            String fechaCompra,
            String fechaSiniestro,
            double esperado
    ) {
        Bien bien = new Bien("BienGen", tipoBien, valorCompra, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSiniestro, "A_NUEVO");

        when(generalAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(aniosMock);

        double resultado = generalStrategy.calcularValorReal(siniestro, 0);

        assertEquals(esperado, resultado, 0.0001, "Debería devolver el valor esperado");
    }

    @ParameterizedTest
    @CsvSource({
            "INFORMATICA, 2027-01-01, 2028-05-01",
            "AUTOMOVIL, 2030-06-01, 2031-10-01"
    })
    @DisplayName("GeneralStrategy: FechaInvalidaException si siniestro < compra")
    void calcularValorReal_FechaInvalida(TipoBien tipoBien, String fechaSin, String fechaCompra) {
        Bien bien = new Bien("BienErrGen", tipoBien, 1000.0, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSin, "A_REAL");

        assertThrows(FechaInvalidaException.class, () -> generalStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si fecha siniestro < fecha compra");
    }

    @DisplayName("GeneralStrategy: SiniestroSinBienesException si lista vacía")
    @ParameterizedTest
    @CsvSource({"A_REAL", "A_NUEVO"})
    void calcularValorReal_SiniestroSinBienes(String tipoIndem) {
        Siniestro siniestro = crearSiniestroOK(List.of(), "2030-01-01", tipoIndem);

        assertThrows(SiniestroSinBienesException.class, () -> generalStrategy.calcularValorReal(siniestro, 1),
                "Debería lanzar excepción si lista de bienes vacía");
    }

    @DisplayName("GeneralStrategy: ValorCompraNegativoException si valor < 0")
    @ParameterizedTest
    @CsvSource({
            "INFORMATICA, -999.0",
            "AUTOMOVIL, -10.0"
    })
    void calcularValorReal_ValorCompraNegativo(TipoBien tipoBien, double valorNeg) {
        Bien bien = new Bien("BienNeg", tipoBien, valorNeg, LocalDate.of(2025, 1, 1));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2028-01-01", "A_REAL");

        assertThrows(ValorCompraNegativoException.class, () -> generalStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si valor compra < 0");
    }

    @DisplayName("GeneralStrategy: TipoIndemnizacionInvalidaException si no es A_REAL / A_NUEVO")
    @ParameterizedTest
    @CsvSource({"AAAA", "XYZ", "SIN_DEFINIR"})
    void calcularValorReal_TipoIndemnizacionInvalida(String tipoInvalido) {
        Bien bien = new Bien("BienInv", TipoBien.MOBILIARIO, 1200.0, LocalDate.of(2020, 1, 1));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2025-01-01", tipoInvalido);

        assertThrows(TipoIndemnizacionInvalidoException.class, () -> generalStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si tipo indemnización no es A_REAL / A_NUEVO");
    }

    private Siniestro crearSiniestroOK(List<Bien> bienes, String fechaSiniestro, String tipoIndemnizacion) {
        Siniestro sin = new Siniestro();
        sin.setAseguradora(Aseguradora.GENERAL);
        sin.setFechaSiniestro(LocalDate.parse(fechaSiniestro));
        sin.setBienesAfectados(bienes);
        sin.setTipoIndemnizacion(tipoIndemnizacion);
        return sin;
    }
}
