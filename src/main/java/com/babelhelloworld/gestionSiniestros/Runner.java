package com.babelhelloworld.gestionSiniestros;

import com.babelhelloworld.gestionSiniestros.models.Aseguradora;
import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.service.ValoracionService;
import com.babelhelloworld.gestionSiniestros.service.ValoracionServiceImpl;

import java.util.Locale;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            ValoracionService valoracionService = new ValoracionServiceImpl();

            boolean salir = false;
            while (!salir) {
                System.out.println("\n=== GESTIÓN DE SINIESTROS ===");
                System.out.println("1. Calcular valor real de un bien");
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

                        Bien bien = new Bien(tipoBien, valorCompra, aniosAmort);

                        System.out.print("Introduce los años de uso (puede ser decimal, ej. 1.5): ");
                        double aniosUso = Double.parseDouble(sc.nextLine());

                        System.out.println("¿Qué compañía aseguradora?");
                        System.out.println(" - GENERAL\n - MAPFRE\n - ALLIANZ\n - MUTUA_MADRILENA");
                        System.out.print("Elige la opción: ");
                        String compania = sc.nextLine().trim().toUpperCase(Locale.ROOT);

                        Aseguradora aseguradora = parseAseguradora(compania);

                        double valorReal = valoracionService.calcular(bien, aniosUso, aseguradora);

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
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static Aseguradora parseAseguradora(String input) {
        return switch (input) {
            case "MAPFRE" -> Aseguradora.MAPFRE;
            case "ALLIANZ" -> Aseguradora.ALLIANZ;
            case "MUTUA_MADRILENA" -> Aseguradora.MUTUA;
            default -> Aseguradora.GENERAL; 
        };
    }
}
