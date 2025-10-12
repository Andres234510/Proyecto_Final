package co.edu.uniquindio.proyecto_final.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtil {


    public static void exportToCSV(List<String> headers, List<List<String>> rows, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            if (headers != null && !headers.isEmpty()) {
                writer.append(String.join(",", headers)).append("\n");
            }

            if (rows != null) {
                for (List<String> row : rows) {
                    writer.append(String.join(",", row)).append("\n");
                }
            }
        }
    }
}
