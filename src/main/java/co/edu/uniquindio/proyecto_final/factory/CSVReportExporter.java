package co.edu.uniquindio.proyecto_final.factory;

import co.edu.uniquindio.proyecto_final.dto.EnvioDTO;
import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.util.CSVUtil;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReportExporter implements ReportExporter {

    @Override
    public void exportEnvios(List<Envio> envios, String path) throws IOException {
        List<String> headers = List.of("ID", "Usuario", "Estado", "Peso", "Costo");
        List<List<String>> rows = new ArrayList<>();

        if (envios != null) {
            for (Envio e : envios) {
                String usuario = (e.getUsuario() != null) ? e.getUsuario().getNombre() : "N/A";
                String estado = (e.getEstado() != null) ? e.getEstado().toString() : "N/A";
                List<String> row = List.of(
                        e.getIdEnvio(),
                        usuario,
                        estado,
                        String.valueOf(e.getPeso()),
                        String.valueOf(e.getCosto())
                );
                rows.add(row);
            }
        }

        CSVUtil.exportToCSV(headers, rows, path);
    }

    @Override
    public void export(ObservableList<EnvioDTO> envioDtos, File file) throws IOException {
        List<String> headers = List.of("ID", "Origen", "Destino", "Estado", "Peso", "Costo", "Fecha");
        List<List<String>> rows = new ArrayList<>();

        if (envioDtos != null) {
            for (EnvioDTO dto : envioDtos) {
                List<String> row = new ArrayList<>();
                row.add(dto.getId() != null ? dto.getId() : "");
                row.add(dto.getOrigen() != null ? dto.getOrigen() : "");
                row.add(dto.getDestino() != null ? dto.getDestino() : "");
                row.add(dto.getEstado() != null ? dto.getEstado() : "");
                row.add(String.valueOf(dto.getPeso()));
                row.add(String.valueOf(dto.getCosto()));
                row.add(dto.getFecha() != null ? dto.getFecha().toString() : "");
                rows.add(row);
            }
        }

        CSVUtil.exportToCSV(headers, rows, file.getAbsolutePath());
    }
}
