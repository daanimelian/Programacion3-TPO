package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.domain.Dog;
import com.programacion3.adoptme.repo.DogRepository;
import com.programacion3.adoptme.service.SortService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/dogs")
public class DogController {
    private final DogRepository repo;
    private final SortService sortService;
    public DogController(DogRepository repo) {
        this.repo = repo;
        this.sortService = new SortService();
    }

    @GetMapping
    public List<Dog> all() {
        return repo.findAll();
    }
    
    @GetMapping("/sort")
    public ResponseEntity<List<Dog>> sortDogs() {
        String criteria = "priority";              // criterio de orden por defecto
        List<Dog> dogs = repo.findAll();           // trae todos de la base
        sortService.sortDogs(dogs, criteria);      // los ordena
        return ResponseEntity.ok(dogs);
    }
    
}

