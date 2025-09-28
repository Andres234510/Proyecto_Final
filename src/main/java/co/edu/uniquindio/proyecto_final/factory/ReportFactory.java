package co.edu.uniquindio.proyecto_final.factory;

public class ReportFactory {
    public static ReportExporter getExporter(String tipo) {
        if ("CSV".equalsIgnoreCase(tipo)) return new CSVReportExporter();
        throw new IllegalArgumentException("Tipo no soportado: " + tipo);
    }
}
