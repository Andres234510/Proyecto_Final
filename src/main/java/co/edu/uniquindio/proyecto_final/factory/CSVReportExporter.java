package co.edu.uniquindio.proyecto_final.factory;

import co.edu.uniquindio.proyecto_final.dto.EnvioDTO;
import co.edu.uniquindio.proyecto_final.util.CSVUtil;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CSVReportExporter implements ReportExporter {

    @Override
    public void export(List<?> data, File target) throws Exception {
        // convertimos a líneas simples si son EnvioDTO
        List<String[]> rows = ((List<?>)data).stream().map(o -> {
            if (o instanceof EnvioDTO) {
                EnvioDTO e = (EnvioDTO)o;
                return new String[] { e.getId(), e.getOrigen(), e.getDestino(), String.valueOf(e.getPeso()),
                        String.valueOf(e.getCosto()), e.getEstado() };
            }
            return new String[] { o.toString() };
        }).collect(Collectors.toList());
        CSVUtil.writeCSV(target, rows);
    }
}
