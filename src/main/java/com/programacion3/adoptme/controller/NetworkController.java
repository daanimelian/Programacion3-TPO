package com.programacion3.adoptme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/network")
public class NetworkController {

    // /network/mst (Prim | Kruskal)
    @GetMapping("/mst")
    public ResponseEntity<String> mst() {
        return ResponseEntity.ok("TODO: MST (Prim/Kruskal)");
    }
}
