package co.edu.uniquindio.proyecto_final.util;

import co.edu.uniquindio.proyecto_final.model.Envio;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {
    public static void exportEnvios(List<Envio> envios, String path) throws IOException {
        try (FileWriter w = new FileWriter(path)) {
            w.append("idEnvio,estado,peso,volumen,costo\n");
            for (Envio e : envios) {
                w.append(String.format("%s,%s,%.2f,%.4f,%.2f\n",
                        e.getIdEnvio(), e.getEstado(), e.getPeso(), e.getVolumen(), e.getCosto()));
            }
        }
    }
}
