package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.domain.Dog;
import com.programacion3.adoptme.repo.DogRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/dogs")
public class DogController {
    private final DogRepository repo;
    public DogController(DogRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Dog> all() { return repo.findAll(); }
    
    @GetMapping("/sort")
    public ResponseEntity<String> sortDogs() {
        return ResponseEntity.ok("TODO: Quick/Merge sort");
    }
    
}

