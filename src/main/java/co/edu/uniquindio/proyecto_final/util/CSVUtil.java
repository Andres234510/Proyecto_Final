package co.edu.uniquindio.proyecto_final.util;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CSVUtil {
    public static void writeCSV(File file, List<String[]> rows) throws Exception {
        try (FileWriter fw = new FileWriter(file)) {
            // header opcional
            for (String[] r : rows) {
                fw.write(String.join(",", escape(r)));
                fw.write("\n");
            }
        }
    }

    private static String[] escape(String[] row) {
        String[] out = new String[row.length];
        for (int i = 0; i < row.length; i++) {
            String s = row[i] == null ? "" : row[i].replaceAll("\"","\"\"");
            if (s.contains(",") || s.contains("\"") || s.contains("\n")) s = "\"" + s + "\"";
            out[i] = s;
        }
        return out;
    }
}
