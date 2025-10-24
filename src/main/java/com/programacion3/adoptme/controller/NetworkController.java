package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.service.GraphLoader;
import com.programacion3.adoptme.service.MSTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/network")
@RequiredArgsConstructor
public class NetworkController {

    private final MSTService mstService;
    private final GraphLoader graphLoader;

    /**
     * Minimum Spanning Tree usando Kruskal
     * Conecta todos los refugios con la menor distancia total
     * GET /network/mst
     */
    @GetMapping("/mst")
    public ResponseEntity<MSTResponse> mst() {
        // Cargar todos los shelters
        var shelterIds = graphLoader.loadAllShelterIds();

        if (shelterIds.isEmpty()) {
            return ResponseEntity.ok(new MSTResponse(
                    "No shelters found",
                    List.of(),
                    0.0
            ));
        }

        // Cargar aristas
        var edges = graphLoader.loadMSTEdges();

        // Ejecutar Kruskal
        var result = mstService.compute(shelterIds, edges);

        // Formatear respuesta
        List<EdgeDTO> formattedEdges = result.edges.stream()
                .map(e -> new EdgeDTO(e.a, e.b, e.weight))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new MSTResponse(
                "MST computed using Kruskal's algorithm",
                formattedEdges,
                result.totalWeight
        ));
    }

    // DTOs para respuesta
    record MSTResponse(
            String message,
            List<EdgeDTO> edges,
            double totalWeight
    ) {}

    record EdgeDTO(
            String from,
            String to,
            double weight
    ) {}
}