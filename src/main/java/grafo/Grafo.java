package grafo;

import java.util.*;

public class Grafo {
    private Map<String, NodoGrafo> nodos;
    private Map<String, Map<String, Double>> adyacencias;

    public Grafo() {
        nodos = new HashMap<>();
        adyacencias = new HashMap<>();
    }

    public boolean insertarNodo(NodoGrafo nodo) {
        String clave = nodo.getClave();
        if (nodos.containsKey(clave)) return false;
        nodos.put(clave, nodo);
        adyacencias.put(clave, new HashMap<>());
        return true;
    }

    public boolean borrarNodo(String clave) {
        if (!nodos.containsKey(clave)) return false;
        nodos.remove(clave);
        adyacencias.remove(clave);
        for (Map<String, Double> vecinos : adyacencias.values()) {
            vecinos.remove(clave);
        }
        return true;
    }

    public boolean existeNodo(String clave) {
        return nodos.containsKey(clave);
    }

    public int numeroNodos() {
        return nodos.size();
    }

    public boolean insertarArista(String clave1, String clave2, Double peso) {
        if (!nodos.containsKey(clave1) || !nodos.containsKey(clave2)) return false;
        if (adyacencias.get(clave1).containsKey(clave2)) return false;

        adyacencias.get(clave1).put(clave2, peso);
        adyacencias.get(clave2).put(clave1, peso);
        return true;
    }

    public boolean borrarArista(String clave1, String clave2) {
        if (!existeArista(clave1, clave2)) return false;
        adyacencias.get(clave1).remove(clave2);
        adyacencias.get(clave2).remove(clave1);
        return true;
    }

    public boolean existeArista(String clave1, String clave2) {
        return adyacencias.containsKey(clave1) &&
               adyacencias.get(clave1).containsKey(clave2);
    }

    public int numeroAristas() {
        int total = 0;
        for (Map<String, Double> vecinos : adyacencias.values()) {
            total += vecinos.size();
        }
        return total / 2; // PORQUE ES NO DIRIGIDO
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String clave : adyacencias.keySet()) {
            sb.append(clave).append(": ");
            Map<String, Double> vecinos = adyacencias.get(clave);
            List<String> conexiones = new ArrayList<>();
            for (String destino : vecinos.keySet()) {
                conexiones.add(destino + "(" + vecinos.get(destino) + ")");
            }
            sb.append(String.join(", ", conexiones)).append("\n");
        }
        return sb.toString();
    }

    public List<Double> Dijkstra(String origen) {
        Map<String, Double> distancias = new HashMap<>();
        for (String clave : nodos.keySet()) {
            distancias.put(clave, Double.MAX_VALUE);
        }
        distancias.put(origen, 0.0);

        PriorityQueue<String> cola = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
        cola.add(origen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            double distanciaActual = distancias.get(actual);
            for (Map.Entry<String, Double> vecino : adyacencias.get(actual).entrySet()) {
                double nuevaDistancia = distanciaActual + vecino.getValue();
                if (nuevaDistancia < distancias.get(vecino.getKey())) {
                    distancias.put(vecino.getKey(), nuevaDistancia);
                    cola.add(vecino.getKey());
                }
            }
        }

        List<Double> resultado = new ArrayList<>();
        for (String clave : nodos.keySet()) {
            resultado.add(distancias.get(clave));
        }
        return resultado;
    }

    public List<String> Dijkstra(String origen, String destino) {
        if (!nodos.containsKey(origen) || !nodos.containsKey(destino)) return null;

        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> anteriores = new HashMap<>();
        Set<String> visitados = new HashSet<>();

        for (String clave : nodos.keySet()) {
            distancias.put(clave, Double.MAX_VALUE);
        }
        distancias.put(origen, 0.0);

        PriorityQueue<String> cola = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
        cola.add(origen);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            if (!visitados.add(actual)) continue;

            for (Map.Entry<String, Double> vecino : adyacencias.get(actual).entrySet()) {
                double nuevaDistancia = distancias.get(actual) + vecino.getValue();
                if (nuevaDistancia < distancias.get(vecino.getKey())) {
                    distancias.put(vecino.getKey(), nuevaDistancia);
                    anteriores.put(vecino.getKey(), actual);
                    cola.add(vecino.getKey());
                }
            }
        }

        List<String> camino = new LinkedList<>();
        String paso = destino;
        while (paso != null) {
            camino.add(0, paso);
            paso = anteriores.get(paso);
        }

        if (!camino.get(0).equals(origen)) return null;
        return camino;
    }
}