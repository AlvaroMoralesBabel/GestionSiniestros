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
class MutuaStrategyTest {

    @Mock
    private AmortizacionService mutuaAmortizacionService;

    private MutuaStrategy mutuaStrategy;

    @BeforeEach
    void setUp() {
        mutuaStrategy = new MutuaStrategy(mutuaAmortizacionService);
    }

    @ParameterizedTest(name = "[{index}] {0} => valor={1}, aniosMock={2}, feCompra={3}, feSin={4}, esperado={5}")
    @CsvSource({
            "AUTOMOVIL, 1500, 2, 2025-01-01, 2035-07-01, 150.0",
            "INFORMATICA, 1000, 5, 2030-01-01, 2030-01-01, 900.0",
            "ELECTRODOMESTICO, 2000, 10, 2020-01-01, 2022-01-01, 1805.0"
    })
    @DisplayName("MutuaStrategy: escenarios paramétricos (residual, etc.)")
    void calcularValorReal_Parametrizado(
            TipoBien tipoBien,
            double valorCompra,
            int aniosMock,
            String fechaCompra,
            String fechaSiniestro,
            double esperado
    ) {
        Bien bien = new Bien("BienMutua", tipoBien, valorCompra, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSiniestro, "A_REAL");

        when(mutuaAmortizacionService.obtenerAniosAmortizacion(eq(bien))).thenReturn(aniosMock);

        double resultado = mutuaStrategy.calcularValorReal(siniestro, 0);

        assertEquals(esperado, resultado, 0.0001, "Deberia calcular el valor esperado");
    }

    @ParameterizedTest
    @CsvSource({
            "AUTOMOVIL, 2030-01-01, 2031-01-01",
            "ELECTRODOMESTICO, 2028-05-10, 2029-01-01"
    })
    @DisplayName("MutuaStrategy: FechaInvalidaException si fecha siniestro < compra")
    void calcularValorReal_FechaInvalida(TipoBien tipoBien, String fechaSin, String fechaCompra) {
        Bien bien = new Bien("BienMutuaErr", tipoBien, 1200.0, LocalDate.parse(fechaCompra));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), fechaSin, "A_REAL");

        assertThrows(FechaInvalidaException.class, () -> mutuaStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si fecha siniestro < fecha compra");
    }

    @DisplayName("MutuaStrategy: lanza SiniestroSinBienesException si lista bienes vacía")
    @ParameterizedTest
    @CsvSource({"A_REAL", "A_NUEVO"})
    void calcularValorReal_SiniestroSinBienes(String tipoIndemnizacion) {
        Siniestro siniestro = crearSiniestroOK(List.of(), "2030-01-01", tipoIndemnizacion);

        assertThrows(SiniestroSinBienesException.class, () -> mutuaStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si siniestro no tiene bienes");
    }

    @DisplayName("MutuaStrategy: ValorCompraNegativoException si valor < 0")
    @ParameterizedTest
    @CsvSource({
            "INFORMATICA, -999.0",
            "AUTOMOVIL, -1"
    })
    void calcularValorReal_ValorCompraNegativo(TipoBien tipoBien, double valorNegativo) {
        Bien bien = new Bien("BienNeg", tipoBien, valorNegativo, LocalDate.of(2020, 1, 1));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2025-01-01", "A_NUEVO");

        assertThrows(ValorCompraNegativoException.class, () -> mutuaStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si valor de compra < 0");
    }

    @DisplayName("MutuaStrategy: TipoIndemnizacionInvalidaException si no es A_REAL/A_NUEVO")
    @ParameterizedTest
    @CsvSource({"X", "SIN_DEFINIR", "XYZ"})
    void calcularValorReal_TipoIndemnizacionInvalida(String tipoInvalido) {
        Bien bien = new Bien("Bien", TipoBien.ELECTRODOMESTICO, 1000.0, LocalDate.of(2020, 1, 1));
        Siniestro siniestro = crearSiniestroOK(List.of(bien), "2022-01-01", tipoInvalido);

        assertThrows(TipoIndemnizacionInvalidoException.class, () -> mutuaStrategy.calcularValorReal(siniestro, 0),
                "Debería lanzar excepción si tipo indemnización no es A_REAL/A_NUEVO");
    }

    private Siniestro crearSiniestroOK(List<Bien> bienes, String fechaSiniestro, String tipoIndemnizacion) {
        Siniestro sin = new Siniestro();
        sin.setAseguradora(Aseguradora.MUTUA);
        sin.setFechaSiniestro(LocalDate.parse(fechaSiniestro));
        sin.setBienesAfectados(bienes);
        sin.setTipoIndemnizacion(tipoIndemnizacion);
        return sin;
    }
}
