package co.edu.uniquindio.proyecto_final.factory;

import java.io.File;
import java.util.List;

public interface ReportExporter {
    void export(List<?> data, File target) throws Exception;
}
