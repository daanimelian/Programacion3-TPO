package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.dto.PathResponse;
import com.programacion3.adoptme.dto.TspResponse;
import com.programacion3.adoptme.service.BnBService;
import com.programacion3.adoptme.service.GraphLoader;
import com.programacion3.adoptme.service.ShortestPathService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RoutesController {

    private final ShortestPathService shortestPathService;
    private final GraphLoader graphLoader;
    private final BnBService bnbService;

    /**
     * Dijkstra - Encuentra el camino más corto considerando distancias
     * GET /routes/shortest?from=A&to=C
     */
    @GetMapping("/shortest")
    public ResponseEntity<PathResponse> shortestRoute(
            @RequestParam String from,
            @RequestParam String to
    ) {
        if (from == null || to == null || from.isBlank() || to.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new PathResponse(false, "Dijkstra", null, 0, 0.0));
        }

        // Cargar aristas con pesos desde Neo4j
        List<ShortestPathService.Edge> edges = graphLoader.loadWeightedEdges();

        // Ejecutar Dijkstra
        var result = shortestPathService.shortestPath(from, to, edges);

        if (result.path.isEmpty() || Double.isInfinite(result.cost)) {
            return ResponseEntity.ok(new PathResponse(false, "Dijkstra", null, 0, 0.0));
        }

        return ResponseEntity.ok(new PathResponse(
                true,
                "Dijkstra",
                result.path,
                result.path.size() - 1,
                result.cost
        ));
    }

    /**
     * TSP Branch & Bound
     * GET /routes/tsp/bnb?nodes=A,B,C,D
     */
    @GetMapping("/tsp/bnb")
    public ResponseEntity<TspResponse> tspBranchBound(
            @RequestParam String nodes
    ) {
        if (nodes == null || nodes.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(TspResponse.builder().route(null).totalDistanceKm(0).build());
        }

        List<String> nodeList = Arrays.stream(nodes.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<ShortestPathService.Edge> edges = graphLoader.loadWeightedEdges();

        List<BnBService.Edge> bnbEdges = edges.stream()
                .map(e -> new BnBService.Edge(e.from, e.to, e.weight))
                .collect(Collectors.toList());

        BnBService.Result result = bnbService.solveTSP(nodeList, bnbEdges);

        return ResponseEntity.ok(
                TspResponse.builder()
                        .route(result.getRoute())
                        .totalDistanceKm((int) Math.round(result.getTotalDistance()))
                        .build()
        );
    }
}
