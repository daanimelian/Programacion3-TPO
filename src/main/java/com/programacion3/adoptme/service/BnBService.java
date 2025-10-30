package com.programacion3.adoptme.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Branch & Bound para TSP (Travelling Salesman Problem)
 * - Entrada: lista de nodos y aristas con peso
 * - Salida: ruta mínima y costo total
 * - Poda usando cota inferior (suma mínima de aristas restantes)
 */
@Service
public class BnBService {

    @Data
    @AllArgsConstructor
    public static class Edge {
        private final String from;
        private final String to;
        private final double weight;
    }

    @Data
    @AllArgsConstructor
    public static class Result {
        private final List<String> route;
        private final double totalDistance;
    }

    /**
     * Encuentra la mejor ruta TSP usando Branch & Bound
     * @param nodes Lista de nodos a visitar
     * @param edges Aristas con peso
     * @return Mejor ruta y distancia total
     */
    public Result solveTSP(List<String> nodes, List<Edge> edges) {
        if (nodes == null || nodes.isEmpty()) return new Result(Collections.emptyList(), 0.0);
        if (nodes.size() == 1) return new Result(List.of(nodes.get(0)), 0.0);

        // Construir mapa de adyacencia rápida
        Map<String, Map<String, Double>> adj = buildAdjacencyMap(edges, nodes);

        double bestCost = Double.POSITIVE_INFINITY;
        List<String> bestRoute = new ArrayList<>();

        // Nodo inicial: se puede elegir cualquiera; aquí el primero
        String start = nodes.get(0);

        // Backtracking BnB
        List<String> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        visited.add(start);
        path.add(start);

        bestRoute = branchAndBound(path, visited, start, adj, 0.0, bestRoute, new double[]{bestCost});
        bestCost = calculateTotalDistance(bestRoute, adj);

        return new Result(bestRoute, bestCost);
    }

    private List<String> branchAndBound(List<String> path,
                                        Set<String> visited,
                                        String current,
                                        Map<String, Map<String, Double>> adj,
                                        double costSoFar,
                                        List<String> bestRoute,
                                        double[] bestCost) {
        if (path.size() == adj.size()) {
            // Cerrar el ciclo volviendo al nodo inicial
            String start = path.get(0);
            double totalCost = costSoFar + adj.get(current).getOrDefault(start, Double.POSITIVE_INFINITY);
            if (totalCost < bestCost[0]) {
                bestCost[0] = totalCost;
                List<String> newBest = new ArrayList<>(path);
                newBest.add(start);
                return newBest;
            }
            return bestRoute;
        }

        for (String next : adj.keySet()) {
            if (visited.contains(next)) continue;
            double edgeCost = adj.get(current).getOrDefault(next, Double.POSITIVE_INFINITY);
            if (edgeCost == Double.POSITIVE_INFINITY) continue;

            double lowerBound = costSoFar + edgeCost + estimateLowerBound(adj, path, visited, next);
            if (lowerBound >= bestCost[0]) continue; // Poda

            // Explorar
            path.add(next);
            visited.add(next);
            bestRoute = branchAndBound(path, visited, next, adj, costSoFar + edgeCost, bestRoute, bestCost);
            path.remove(path.size() - 1);
            visited.remove(next);
        }
        return bestRoute;
    }

    /**
     * Cota inferior: suma mínima de aristas salientes de nodos no visitados
     */
    private double estimateLowerBound(Map<String, Map<String, Double>> adj,
                                      List<String> path,
                                      Set<String> visited,
                                      String current) {
        double bound = 0.0;
        for (String node : adj.keySet()) {
            if (visited.contains(node)) continue;
            // Tomamos la arista mínima que sale del nodo
            double minEdge = adj.get(node).values().stream()
                    .filter(w -> w > 0)
                    .min(Double::compare)
                    .orElse(0.0);
            bound += minEdge;
        }
        return bound;
    }

    private Map<String, Map<String, Double>> buildAdjacencyMap(List<Edge> edges, List<String> nodes) {
        Map<String, Map<String, Double>> adj = new HashMap<>();
        for (String n : nodes) adj.put(n, new HashMap<>());
        for (Edge e : edges) {
            if (!adj.containsKey(e.getFrom()) || !adj.containsKey(e.getTo())) continue;
            adj.get(e.getFrom()).put(e.getTo(), e.getWeight());
            adj.get(e.getTo()).put(e.getFrom(), e.getWeight()); // No dirigido
        }
        return adj;
    }

    private double calculateTotalDistance(List<String> route, Map<String, Map<String, Double>> adj) {
        if (route == null || route.size() < 2) return 0.0;
        double total = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            total += adj.get(route.get(i)).getOrDefault(route.get(i + 1), 0.0);
        }
        return total;
    }
}
