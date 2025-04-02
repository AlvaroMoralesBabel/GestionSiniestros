package com.babelhelloworld.gestionSiniestros.service.siniestro;

import com.babelhelloworld.gestionSiniestros.exceptions.SiniestroSinBienesException;
import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import com.babelhelloworld.gestionSiniestros.service.valoracion.ValoracionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestionSiniestrosServiceImplTest {

    @Mock
    private ValoracionService valoracionService;

    private GestionSiniestrosService gestionService;

    @BeforeEach
    void setUp() {
        gestionService = new GestionSiniestrosServiceImpl(valoracionService);
    }

    @Test
    @DisplayName("GestionSiniestrosService: calcula correctamente para varios bienes")
    void calcularValores_paraVariosBienes_devuelveMapaCorrecto() {
        // Arrange
        Bien bien1 = new Bien("TV", TipoBien.ELECTRODOMESTICO, 1000, LocalDate.of(2020, 1, 1));
        Bien bien2 = new Bien("PC", TipoBien.INFORMATICA, 1500, LocalDate.of(2019, 6, 1));

        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.GENERAL);
        siniestro.setFechaSiniestro(LocalDate.of(2024, 1, 1));
        siniestro.setBienesAfectados(List.of(bien1, bien2));

        when(valoracionService.calcular(siniestro, 0)).thenReturn(700.0);
        when(valoracionService.calcular(siniestro, 1)).thenReturn(850.0);

        // Act
        Map<Bien, Double> resultado = gestionService.procesarSiniestro(siniestro);

        // Assert
        assertEquals(2, resultado.size(), "Debe haber 2 bienes en el mapa");
        assertEquals(700.0, resultado.get(bien1), "Valor del bien1 incorrecto");
        assertEquals(850.0, resultado.get(bien2), "Valor del bien2 incorrecto");
    }

    @Test
    @DisplayName("GestionSiniestrosService: lanza excepción si no hay bienes")
    void calcularValores_siniestroSinBienes_lanzaExcepcion() {
        // Arrange
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.ALLIANZ);
        siniestro.setFechaSiniestro(LocalDate.of(2030, 1, 1));
        siniestro.setBienesAfectados(List.of()); // lista vacía

        // Act & Assert
        assertThrows(SiniestroSinBienesException.class,
                () -> gestionService.procesarSiniestro(siniestro),
                "Debe lanzar excepción si no hay bienes");
    }

    @Test
    @DisplayName("GestionSiniestrosService: propaga excepción lanzada por ValoracionService")
    void calcularValores_valoracionServiceLanzaExcepcion_propagaExcepcion() {
        // Arrange
        Bien bien1 = new Bien("TV", TipoBien.ELECTRODOMESTICO, 1000, LocalDate.of(2020, 1, 1));
        Siniestro siniestro = new Siniestro();
        siniestro.setAseguradora(Aseguradora.ALLIANZ);
        siniestro.setFechaSiniestro(LocalDate.of(2024, 1, 1));
        siniestro.setBienesAfectados(List.of(bien1));

        when(valoracionService.calcular(siniestro, 0))
                .thenThrow(new RuntimeException("Error interno en estrategia"));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gestionService.procesarSiniestro(siniestro));

        assertEquals("Error interno en estrategia", ex.getMessage());
    }
}
