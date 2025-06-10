package grafo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        String path = "src/main/resources/distancias-gasolineras.csv";

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String clave1 = partes[0];
                    String clave2 = partes[1];
                    double distancia = Double.parseDouble(partes[2].replace(",", "."));

                    if (!grafo.existeNodo(clave1)) {
                        grafo.insertarNodo(new Gasolinera(clave1, "", "", "", ""));
                    }
                    if (!grafo.existeNodo(clave2)) {
                        grafo.insertarNodo(new Gasolinera(clave2, "", "", "", ""));
                    }

                    grafo.insertarArista(clave1, clave2, distancia);
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo CSV: " + e.getMessage());
        }

        System.out.println("Grafo creado:");
        System.out.println(grafo);

        // VALORES DE ORIGEN Y DESTINO
        String origen = "AVILÉS-15";
        String destino = "GIJÓN-2";

        List<String> camino = grafo.Dijkstra(origen, destino);
        if (camino != null) {
            System.out.println("Camino más corto de " + origen + " a " + destino + ": " + camino);
        } else {
            System.out.println("No se encontró camino entre " + origen + " y " + destino);
        }
    }
}