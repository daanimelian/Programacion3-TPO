package com.programacion3.adoptme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routes")
public class RoutesController {

    // /routes/shortest (Dijkstra)
    @GetMapping("/shortest")
    public ResponseEntity<String> shortestRoute() {
        // Implementación del algoritmo de Dijkstra para encontrar la ruta más corta


        return ResponseEntity.ok("TODO: Dijkstra shortest path");
    }

    // /routes/tsp/bnb (Branch & Bound)
    @GetMapping("/tsp/bnb")
    public ResponseEntity<String> tspBranchBound() {
        return ResponseEntity.ok("TODO: TSP Branch & Bound");
    }
}
