package com.programacion3.adoptme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
public class GraphController {

    // /graph/reachable (BFS/DFS)
    @GetMapping("/reachable")
    public ResponseEntity<String> reachable() {
        return ResponseEntity.ok("TODO: BFS/DFS reachable");
    }
}
