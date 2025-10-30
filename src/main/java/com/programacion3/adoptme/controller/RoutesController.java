package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.dto.PathResponse;
import com.programacion3.adoptme.dto.TspResponse;
import com.programacion3.adoptme.service.GraphLoader;
import com.programacion3.adoptme.service.ShortestPathService;
import com.programacion3.adoptme.service.TspService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RoutesController {

    private final ShortestPathService shortestPathService;
    private final GraphLoader graphLoader;
    private final TspService tspService;

    /**
     * Dijkstra - Encuentra el camino más corto considerando distancias
     * GET /routes/shortest?from=A&to=C
     */
    @GetMapping("/shortest")
    public ResponseEntity<PathResponse> shortestRoute(
            @RequestParam String from,
            @RequestParam String to
    ) {
        // Validaciones
        if (from == null || to == null || from.isBlank() || to.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new PathResponse(false, "Dijkstra", null, 0, 0.0));
        }

        // Cargar aristas con pesos desde Neo4j
        var edges = graphLoader.loadWeightedEdges();

        // Ejecutar Dijkstra
        var result = shortestPathService.shortestPath(from, to, edges);

        // Si no hay camino
        if (result.path.isEmpty() || Double.isInfinite(result.cost)) {
            return ResponseEntity.ok(new PathResponse(
                    false,
                    "Dijkstra",
                    null,
                    0,
                    0.0
            ));
        }

        // Respuesta exitosa
        return ResponseEntity.ok(new PathResponse(
                true,
                "Dijkstra",
                result.path,
                result.path.size() - 1,
                result.cost
        ));
    }

    /**
     * TSP con Branch & Bound - TODO: Implementar en Fase 2
     * GET /routes/tsp/bnb?nodes=A,B,C,H
     */
    @GetMapping("/tsp/bnb")
    
    public ResponseEntity<TspResponse> tspBranchBound(
            @RequestParam String nodes
    ) {
        if (nodes == null || nodes.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Parsear los nodos de la query string
        List<String> nodeList = Arrays.stream(nodes.split(","))
                .map(String::trim)
                .toList();

        // Ejecutar algoritmo Branch & Bound
        TspResponse result = tspService.solveTsp(nodeList);

        return ResponseEntity.ok(result);
    }
}