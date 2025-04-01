package com.babelhelloworld.gestionSiniestros;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import com.babelhelloworld.gestionSiniestros.service.ValoracionService;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

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
                System.out.println("1. Calcular valor real de un bien (fecha)");
                System.out.println("2. Salir");
                System.out.print("Elige una opción: ");

                String opcion = sc.nextLine();
                switch (opcion) {
                    case "1" -> {
                        System.out.print("Introduce el tipo de bien: ");
                        String tipoBien = sc.nextLine();

                        System.out.print("Introduce el valor de compra del bien: ");
                        double valorCompra = Double.parseDouble(sc.nextLine());

                        System.out.print("Introduce los años de amortización (entero): ");
                        int aniosAmort = Integer.parseInt(sc.nextLine());

                        System.out.print("Introduce la fecha de compra (yyyy-MM-dd): ");
                        LocalDate fechaCompra = LocalDate.parse(sc.nextLine());

                        Bien bien = new Bien(tipoBien, valorCompra, aniosAmort, fechaCompra);

                        System.out.print("Introduce la fecha del siniestro (yyyy-MM-dd): ");
                        LocalDate fechaSiniestro = LocalDate.parse(sc.nextLine());

                        Siniestro siniestro = new Siniestro();
                        siniestro.setBienAfectado(bien);
                        siniestro.setFechaSiniestro(fechaSiniestro);

                        System.out.println("¿Qué compañía aseguradora?");
                        System.out.println(" - GENERAL\n - MAPFRE\n - ALLIANZ\n - MUTUA_MADRILENA");
                        System.out.print("Elige la opción: ");
                        String compania = sc.nextLine().trim().toUpperCase(Locale.ROOT);

                        Aseguradora aseguradora = parseAseguradora(compania);

                        double valorReal = valoracionService.calcular(siniestro, aseguradora);

                        System.out.println("Valor real calculado: " + valorReal + " €");
                    }
                    case "2" -> {
                        System.out.println("Saliendo de la aplicación...");
                        salir = true;
                    }
                    default -> {
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
