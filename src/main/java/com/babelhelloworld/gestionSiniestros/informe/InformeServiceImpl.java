package com.babelhelloworld.gestionSiniestros.informe;

import com.babelhelloworld.gestionSiniestros.models.Bien;
import com.babelhelloworld.gestionSiniestros.models.Siniestro;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class InformeServiceImpl implements InformeService {

    @Override
    public void printToConsole(Siniestro siniestro, Map<Bien, Double> valorIndividual) {
        double total = valorIndividual.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("\n=== INFORME DEL SINIESTRO ===");
        System.out.println("Póliza: " + siniestro.getNumeroPoliza());
        System.out.println("Fecha siniestro: " + siniestro.getFechaSiniestro());
        System.out.println("Dirección: " + siniestro.getDireccion());
        System.out.println("Aseguradora: " + siniestro.getAseguradora().getNombre());
        System.out.println("Tipo indemnización: " + siniestro.getTipoIndemnizacion());
        System.out.println("Bienes afectados: " + valorIndividual.size());
        System.out.println("Valor por cada bien:");
        valorIndividual.forEach((bien, valor) ->
                System.out.printf(" - %s: %.2f€ %n", bien.getNombre(), valor)
        );
        System.out.printf("Valor total: %.2f€ %n", total);
    }

    @Override
    public String printToFile(Siniestro siniestro, Map<Bien, Double> valorIndividual) throws IOException {
        File directorio = new File("src/main/resources/informes");
        File tempFile = File.createTempFile("siniestro-", ".txt", directorio);
        try (PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
            double total = valorIndividual.values().stream().mapToDouble(Double::doubleValue).sum();
            pw.println("=== INFORME DEL SINIESTRO ===");
            pw.println("Póliza: " + siniestro.getNumeroPoliza());
            pw.println("Fecha siniestro: " + siniestro.getFechaSiniestro());
            pw.println("Dirección: " + siniestro.getDireccion());
            pw.println("Aseguradora: " + siniestro.getAseguradora().getNombre());
            pw.println("Tipo indemnización: " + siniestro.getTipoIndemnizacion());
            pw.println("Bienes afectados: " + valorIndividual.size());
            pw.println("Valor por cada bien:");
            valorIndividual.forEach((bien, valor) ->
                    pw.printf(" - %s: %.2f€ %n", bien.getNombre(), valor)
            );
            pw.printf("Valor total: %.2f€ %n", total);
        }
        return tempFile.getAbsolutePath();
    }
}
