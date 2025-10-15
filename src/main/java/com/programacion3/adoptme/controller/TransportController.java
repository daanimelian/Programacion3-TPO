package com.programacion3.adoptme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transport")
public class TransportController {

    // /transport/optimal-dp (DP / Knapsack)
    @GetMapping("/optimal-dp")
    public ResponseEntity<String> optimalTransport() {
        return ResponseEntity.ok("TODO: Optimal transport DP/Knapsack");
    }
}
