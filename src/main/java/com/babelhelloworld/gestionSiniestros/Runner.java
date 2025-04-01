package com.babelhelloworld.gestionSiniestros;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.models.TipoBien;
import com.babelhelloworld.gestionSiniestros.service.valoracion.ValoracionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
public class Runner implements CommandLineRunner {

    private final ValoracionService valoracionService;

    public Runner(ValoracionService valoracionService) {
        this.valoracionService = valoracionService;
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

            System.out.print("Introduce la fecha del siniestro (yyyy-MM-dd): ");
            LocalDate fechaSiniestro = LocalDate.parse(sc.nextLine());
            siniestro.setFechaSiniestro(fechaSiniestro);

            System.out.print("Introduce la dirección del siniestro: ");
            siniestro.setDireccion(sc.nextLine().trim());

            System.out.println("¿Qué compañía aseguradora?");
            System.out.println(" - GENERAL\n - MAPFRE\n - ALLIANZ\n - MUTUA_MADRILENA");
            System.out.print("Elige la opción: ");
            String companiaStr = sc.nextLine().trim().toUpperCase(Locale.ROOT);
            Aseguradora aseguradora = parseAseguradora(companiaStr);
            siniestro.setAseguradora(aseguradora);

            System.out.print("Tipo de indemnización (A_REAL / A_NUEVO): ");
            siniestro.setTipoIndemnizacion(sc.nextLine().trim().toUpperCase(Locale.ROOT));

            List<Bien> bienes = new ArrayList<>();
            System.out.print("¿Cuántos bienes afectados quieres añadir? ");
            int numBienes = Integer.parseInt(sc.nextLine().trim());

            for (int i = 1; i <= numBienes; i++) {
                System.out.println("\n=== Bien " + i + " ===");

                System.out.print("Introduce el nombre del bien: ");
                String nombre = sc.nextLine();

                System.out.print("Introduce el tipo de bien" + Arrays.toString(TipoBien.values()) + ": ");
                TipoBien tipoBien = parseTipoBien(sc.nextLine().trim().toUpperCase(Locale.ROOT));

                System.out.print("Introduce el valor de compra del bien: ");
                double valorCompra = Double.parseDouble(sc.nextLine().trim());

                System.out.print("Introduce la fecha de compra (yyyy-MM-dd): ");
                LocalDate fechaCompra = LocalDate.parse(sc.nextLine().trim());

                Bien bien = new Bien(nombre, tipoBien, valorCompra, fechaCompra);

                bien.setTipo(tipoBien);
                bienes.add(bien);
            }

            siniestro.setBienesAfectados(bienes);

            double valorTotal = valoracionService.calcular(siniestro, siniestro.getAseguradora());
            System.out.println("\n=== RESULTADO DEL SINIESTRO ===");
            System.out.println("Poliza: " + siniestro.getNumeroPoliza());
            System.out.println("Aseguradora: " + siniestro.getAseguradora().getNombre());
            System.out.println("Tipo Indemnización: " + siniestro.getTipoIndemnizacion());
            System.out.println("Fecha Siniestro: " + siniestro.getFechaSiniestro());
            System.out.println("Bienes afectados: " + bienes.size());
            System.out.println("Valor total calculado: " + valorTotal + " €");

        } catch (DateTimeParseException e) {
            System.err.println("Formato de fecha inválido. Vuelve a intentarlo.");
        } catch (NumberFormatException e) {
            System.err.println("Formato numérico inválido. Vuelve a intentarlo.");
        } catch (Exception e) {
            System.err.println("Error al crear siniestro: " + e.getMessage());
        }
    }

    private Aseguradora parseAseguradora(String input) {
        return switch (input) {
            case "MAPFRE" -> Aseguradora.MAPFRE;
            case "ALLIANZ" -> Aseguradora.ALLIANZ;
            case "MUTUA_MADRILENA" -> Aseguradora.MUTUA;
            default -> Aseguradora.GENERAL;
        };
    }

    private TipoBien parseTipoBien(String input) {
        return switch (input) {
            case "ELECTRODOMESTICO" -> TipoBien.ELECTRODOMESTICO;
            case "AUTOMOVIL" -> TipoBien.AUTOMOVIL;
            case "INFORMATICA" -> TipoBien.INFORMATICA;
            case "MOBILIARIO" -> TipoBien.MOBILIARIO;
            default -> TipoBien.OTRO;
        };
    }
}
