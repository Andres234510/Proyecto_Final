package co.edu.uniquindio.proyecto_final.factory;

import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.dto.EnvioDTO;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ReportExporter {

    void exportEnvios(List<Envio> envios, String path) throws IOException;


    void export(ObservableList<EnvioDTO> envioDtos, File file) throws IOException;
}
