package com.programacion3.adoptme.service;

import com.programacion3.adoptme.dto.TspResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TspService {

    // Matriz de distancias simulada
    private final Map<String, Map<String, Integer>> distanceMatrix = Map.of(
            "A", Map.of("B", 10, "C", 15, "D", 20),
            "B", Map.of("A", 10, "C", 35, "D", 25),
            "C", Map.of("A", 15, "B", 35, "D", 30),
            "D", Map.of("A", 20, "B", 25, "C", 30)
    );

    private int bestCost;
    private List<String> bestRoute;

    public TspResponse solveTsp(List<String> nodes) {
        if (nodes == null || nodes.size() <= 1) {
            return TspResponse.builder()
                    .route(nodes)
                    .totalDistanceKm(0)
                    .build();
        }

        bestCost = Integer.MAX_VALUE;
        bestRoute = new ArrayList<>();

        boolean[] visited = new boolean[nodes.size()];
        List<String> currentPath = new ArrayList<>();

        visited[0] = true;
        currentPath.add(nodes.get(0));

        branchAndBound(nodes, visited, currentPath, 0, 0);

        return TspResponse.builder()
                .route(bestRoute)
                .totalDistanceKm(bestCost)
                .build();
    }

    private void branchAndBound(List<String> nodes, boolean[] visited,
                                List<String> currentPath, int level, int currentCost) {

        if (level == nodes.size() - 1) {
            String last = currentPath.get(currentPath.size() - 1);
            String start = nodes.get(0);

            if (distanceMatrix.containsKey(last) && distanceMatrix.get(last).containsKey(start)) {
                int totalCost = currentCost + distanceMatrix.get(last).get(start);

                if (totalCost < bestCost) {
                    bestCost = totalCost;
                    bestRoute = new ArrayList<>(currentPath);
                    bestRoute.add(start);
                }
            }
            return;
        }

        String current = currentPath.get(currentPath.size() - 1);

        for (int i = 0; i < nodes.size(); i++) {
            if (!visited[i]) {
                String next = nodes.get(i);
                if (!distanceMatrix.containsKey(current)
                        || !distanceMatrix.get(current).containsKey(next)) continue;

                int newCost = currentCost + distanceMatrix.get(current).get(next);

                if (newCost < bestCost) { // poda
                    visited[i] = true;
                    currentPath.add(next);

                    branchAndBound(nodes, visited, currentPath, level + 1, newCost);

                    visited[i] = false;
                    currentPath.remove(currentPath.size() - 1);
                }
            }
        }
    }
}
