package com.programacion3.adoptme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adoptions")
public class AdoptionsController {

    // /adoptions/greedy (Greedy)
    @GetMapping("/greedy")
    public ResponseEntity<String> greedyAdoption() {
        return ResponseEntity.ok("TODO: Greedy adoption algorithm");
    }

    // /adoptions/constraints/backtracking (Backtracking)
    @GetMapping("/constraints/backtracking")
    public ResponseEntity<String> backtrackingAdoption() {
        return ResponseEntity.ok("TODO: Backtracking adoption constraints");
    }
}
