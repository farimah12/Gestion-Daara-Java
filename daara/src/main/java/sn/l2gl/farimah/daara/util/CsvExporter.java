package sn.l2gl.farimah.daara.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvExporter {

    public static void exporter(String cheminFichier, String[] entetes, List<String[]> lignes) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(cheminFichier))) {
            pw.println(String.join(";", entetes));
            for (String[] ligne : lignes) {
                pw.println(String.join(";", ligne));
            }
        }
    }
}