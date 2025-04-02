package com.babelhelloworld.gestionSiniestros;

import com.babelhelloworld.gestionSiniestros.informe.InformeService;
import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import com.babelhelloworld.gestionSiniestros.service.siniestro.GestionSiniestrosService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
public class Runner implements CommandLineRunner {

    private final GestionSiniestrosService gestionSiniestrosService;

    private final InformeService informeService;

    public Runner(GestionSiniestrosService gestionSiniestrosService, InformeService informeService) {
        this.gestionSiniestrosService = gestionSiniestrosService;
        this.informeService = informeService;
    }

    @Override
    public void run(String... args) {
        try (Scanner sc = new Scanner(System.in)) {
            boolean salir = false;
            while (!salir) {
                System.out.println("\n=== GESTIÓN DE SINIESTROS ===");
                System.out.println("1. Crear y calcular valor de siniestro");
                System.out.println("2. Salir");
                System.out.print("Elige una opción: ");

                String opcion = sc.nextLine().trim();
                switch (opcion) {
                    case "1" -> crearSiniestro(sc);
                    case "2" -> {
                        System.out.println("Saliendo de la aplicación...");
                        salir = true;
                    }
                    default -> System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearSiniestro(Scanner sc) {
        try {
            Siniestro siniestro = new Siniestro();

            System.out.print("Introduce el número de póliza: ");
            siniestro.setNumeroPoliza(sc.nextLine().trim());

            LocalDate fechaSiniestro = leerFecha(sc, "Introduce la fecha del siniestro (yyyy-MM-dd): ");
            siniestro.setFechaSiniestro(fechaSiniestro);

            System.out.print("Introduce la dirección del siniestro: ");
            siniestro.setDireccion(sc.nextLine().trim());

            Aseguradora aseguradora = leerAseguradora(sc);
            siniestro.setAseguradora(aseguradora);

            System.out.print("Tipo de indemnización (A_REAL / A_NUEVO): ");
            siniestro.setTipoIndemnizacion(sc.nextLine().trim().toUpperCase(Locale.ROOT));

            List<Bien> bienes = leerListaBienes(sc);
            siniestro.setBienesAfectados(bienes);

            Map<Bien, Double> resultado = gestionSiniestrosService.procesarSiniestro(siniestro);

            System.out.println("Desea imprimir el informe por consola (1) o en un fichero temporal (2)?");
            String modo = sc.nextLine().trim();
            if ("2".equals(modo)) {
                try {
                    String filePath = informeService.printToFile(siniestro, resultado);
                    System.out.println("Informe guardado en " + filePath);
                } catch (IOException e) {
                    System.err.println("Error al generar fichero: " + e.getMessage());
                }
            } else {
                informeService.printToConsole(siniestro, resultado);
            }


        } catch (DateTimeParseException e) {
            System.err.println("Formato de fecha inválido. Vuelve a intentarlo.");
        } catch (NumberFormatException e) {
            System.err.println("Formato numérico inválido. Vuelve a intentarlo.");
        } catch (Exception e) {
            System.err.println("Error al crear siniestro: " + e.getMessage());
        }
    }

    private LocalDate leerFecha(Scanner sc, String prompt) {
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                fecha = LocalDate.parse(input);
                valido = true;
            } catch (DateTimeParseException e) {
                System.err.println("Fecha inválida. Inténtalo de nuevo.");
            }
        }
        return fecha;
    }

    private Aseguradora leerAseguradora(Scanner sc) {
        System.out.println("¿Qué compañía aseguradora?");
        System.out.println(" - GENERAL\n - MAPFRE\n - ALLIANZ\n - MUTUA_MADRILENA");
        System.out.print("Elige la opción: ");
        String input = sc.nextLine().trim().toUpperCase(Locale.ROOT);
        return switch (input) {
            case "MAPFRE" -> Aseguradora.MAPFRE;
            case "ALLIANZ" -> Aseguradora.ALLIANZ;
            case "MUTUA_MADRILENA" -> Aseguradora.MUTUA;
            default -> Aseguradora.GENERAL;
        };
    }

    private List<Bien> leerListaBienes(Scanner sc) {
        List<Bien> bienes = new ArrayList<>();
        System.out.print("¿Cuántos bienes afectados quieres añadir? ");
        int numBienes = Integer.parseInt(sc.nextLine().trim());

        for (int i = 1; i <= numBienes; i++) {
            System.out.println("\n=== Bien " + i + " ===");

            System.out.print("Introduce el nombre del bien: ");
            String nombre = sc.nextLine().trim();

            TipoBien tipoBien = leerTipoBien(sc);

            double valorCompra = leerDouble(sc, "Introduce el valor de compra del bien: ");

            LocalDate fechaCompra = leerFecha(sc, "Introduce la fecha de compra (yyyy-MM-dd): ");

            Bien bien = new Bien(nombre, tipoBien, valorCompra, fechaCompra);
            bienes.add(bien);
        }

        return bienes;
    }

    private TipoBien leerTipoBien(Scanner sc) {
        while (true) {
            System.out.print("Introduce el tipo de bien " + Arrays.toString(TipoBien.values()) + ": ");
            String input = sc.nextLine().trim().toUpperCase(Locale.ROOT);
            try {
                return switch (input) {
                    case "ELECTRODOMESTICO" -> TipoBien.ELECTRODOMESTICO;
                    case "AUTOMOVIL" -> TipoBien.AUTOMOVIL;
                    case "INFORMATICA" -> TipoBien.INFORMATICA;
                    case "MOBILIARIO" -> TipoBien.MOBILIARIO;
                    default -> TipoBien.OTRO;
                };
            } catch (Exception e) {
                System.err.println("Tipo de bien no válido, prueba de nuevo.");
            }
        }
    }

    private double leerDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.err.println("Error: valor numérico inválido. Vuelve a intentarlo.");
            }
        }
    }
}
