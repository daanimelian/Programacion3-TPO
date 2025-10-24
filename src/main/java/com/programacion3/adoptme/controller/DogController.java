package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.domain.Dog;
import com.programacion3.adoptme.repo.DogRepository;
import com.programacion3.adoptme.service.SortService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dogs")
@RequiredArgsConstructor
public class DogController {
    private final DogRepository repo;
    private final SortService sortService;
    public DogController(DogRepository repo) {
        this.repo = repo;
        this.sortService = new SortService();
    }

    private final DogRepository dogRepository;
    private final SortService sortService;

    /**
     * Lista todos los perros
     * GET /dogs
     */
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
        return dogRepository.findAll();
    }

    /**
     * Ordena perros usando MergeSort (Divide y Vencerás)
     * GET /dogs/sort?criteria=priority
     * GET /dogs/sort?criteria=age
     * GET /dogs/sort?criteria=weight
     */
    @GetMapping("/sort")
    public ResponseEntity<?> sortDogs(
            @RequestParam(defaultValue = "priority") String criteria
    ) {
        try {
            // Obtener todos los perros
            List<Dog> dogs = dogRepository.findAll();

            if (dogs.isEmpty()) {
                return ResponseEntity.ok(new SortResponse(
                        "No dogs found",
                        criteria,
                        List.of()
                ));
            }

            // Ordenar usando el servicio
            sortService.sortDogs(dogs, criteria);

            return ResponseEntity.ok(new SortResponse(
                    "Dogs sorted by " + criteria + " using MergeSort",
                    criteria,
                    dogs
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", e.getMessage(),
                            "validCriteria", List.of("priority", "age", "weight")
                    ));
        }
    }

    // DTO para respuesta
    record SortResponse(
            String message,
            String criteria,
            List<Dog> dogs
    ) {}
}