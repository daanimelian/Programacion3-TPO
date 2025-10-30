package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.domain.Dog;
import com.programacion3.adoptme.exception.ResourceNotFoundException;
import com.programacion3.adoptme.repo.AdopterRepository;
import com.programacion3.adoptme.repo.DogRepository;
import com.programacion3.adoptme.service.ScorerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/adoptions")
@RequiredArgsConstructor
public class AdoptionsController {

    private final AdopterRepository adopterRepository;
    private final DogRepository dogRepository;
    private final ScorerService scorerService;

    /**
     * Algoritmo Greedy: Asigna perros a un adoptante maximizando el score
     * Considera: presupuesto, jardín, niños, energía del perro
     * GET /adoptions/greedy?adopterId=P1
     */
    @GetMapping("/greedy")
    public ResponseEntity<GreedyResponse> greedyAdoption(
            @RequestParam String adopterId
    ) {
        // Buscar adoptante
        var adopter = adopterRepository.findById(adopterId)
                .orElseThrow(() -> new ResourceNotFoundException("Adopter not found: " + adopterId));

        // Obtener todos los perros disponibles
        List<Dog> allDogs = dogRepository.findAll();

        if (allDogs.isEmpty()) {
            return ResponseEntity.ok(new GreedyResponse(
                    "No dogs available for adoption",
                    adopterId,
                    adopter.getName(),
                    List.of(),
                    0.0,
                    0.0
            ));
        }

        // Convertir a formato del ScorerService
        List<ScorerService.Dog> candidates = allDogs.stream()
                .map(d -> new ScorerService.Dog(
                        d.getId(),
                        d.getGoodWithKids() != null && d.getGoodWithKids(),
                        "LARGE".equalsIgnoreCase(d.getSize()), // perros grandes necesitan jardín
                        mapEnergy(d.getEnergy()),
                        mapSize(d.getSize()),
                        estimateCost(d) // costo estimado por adopción
                ))
                .collect(Collectors.toList());

        // Ejecutar algoritmo Greedy
        var result = scorerService.scoreAndAssign(
                candidates,
                adopter.getHasKids() != null && adopter.getHasKids(),
                adopter.getHasYard() != null && adopter.getHasYard(),
                adopter.getMaxDogs() != null ? adopter.getMaxDogs() : 1,
                adopter.getBudget() != null ? adopter.getBudget() : 20000.0
        );

        // Formatear respuesta
        List<AssignedDog> assigned = result.assigned.stream()
                .map(d -> new AssignedDog(d.id, findDogName(allDogs, d.id), d.cost))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new GreedyResponse(
                "Greedy algorithm executed successfully",
                adopterId,
                adopter.getName(),
                assigned,
                result.totalScore,
                result.totalCost
        ));
    }

    /**
     * Asigna perros considerando TODAS las restricciones posibles
     */
    @GetMapping("/constraints/backtracking")
    public ResponseEntity<BacktrackingResponse> backtrackingAdoption(
            @RequestParam String adopterId,
            @RequestParam(defaultValue = "false") boolean all
    ) {
        var adopter = adopterRepository.findById(adopterId)
                .orElseThrow(() -> new ResourceNotFoundException("Adopter not found: " + adopterId));

        List<Dog> allDogs = dogRepository.findAll();
        if (allDogs.isEmpty()) {
            return ResponseEntity.ok(new BacktrackingResponse(
                    "No dogs available",
                    adopterId,
                    adopter.getName(),
                    List.of()
            ));
        }

        List<List<AssignedDog>> results = new ArrayList<>();
        backtrack(new ArrayList<>(), allDogs, 0, adopter, results, all);

        return ResponseEntity.ok(new BacktrackingResponse(
                results.isEmpty() ? "No valid combinations found" : "Backtracking executed",
                adopterId,
                adopter.getName(),
                results
        ));
    }

    // --- Backtracking core ---
    private void backtrack(List<Dog> current, List<Dog> candidates, int start,
                           com.programacion3.adoptme.domain.Adopter adopter,
                           List<List<AssignedDog>> results, boolean all) {
        // Validar cantidad máxima
        if (current.size() > (adopter.getMaxDogs() != null ? adopter.getMaxDogs() : 1)) return;

        // Validar restricciones de cada perro
        for (Dog d : current) {
            if ((adopter.getHasKids() != null && adopter.getHasKids()) && (d.getGoodWithKids() == null || !d.getGoodWithKids())) return;
            if ((adopter.getHasYard() != null && !adopter.getHasYard()) && "LARGE".equalsIgnoreCase(d.getSize())) return;
        }

        if (!current.isEmpty()) {
            // Agregar combinación válida
            List<AssignedDog> combination = current.stream()
                    .map(d -> new AssignedDog(d.getId(), d.getName(), estimateCost(d)))
                    .collect(Collectors.toList());
            results.add(combination);
            if (!all) return; // solo la primera solución
        }

        for (int i = start; i < candidates.size(); i++) {
            current.add(candidates.get(i));
            backtrack(current, candidates, i + 1, adopter, results, all);
            current.remove(current.size() - 1);
            if (!all && !results.isEmpty()) return; // corta al encontrar la primera
        }
    }

    // --- Métodos auxiliares ---
    private int mapEnergy(String energy) {
        if (energy == null) return 5;
        return switch (energy.toUpperCase()) {
            case "LOW" -> 2;
            case "MEDIUM" -> 5;
            case "HIGH" -> 8;
            default -> 5;
        };
    }

    private int mapSize(String size) {
        if (size == null) return 2;
        return switch (size.toUpperCase()) {
            case "SMALL" -> 1;
            case "MEDIUM" -> 2;
            case "LARGE" -> 3;
            default -> 2;
        };
    }

    private double estimateCost(Dog dog) {
        double baseCost = 5000.0;
        double sizeCost = mapSize(dog.getSize()) * 2000.0;
        double specialNeedsCost = (dog.getSpecialNeeds() != null && dog.getSpecialNeeds()) ? 5000.0 : 0.0;
        return baseCost + sizeCost + specialNeedsCost;
    }

    private String findDogName(List<Dog> dogs, String id) {
        return dogs.stream()
                .filter(d -> d.getId().equals(id))
                .map(Dog::getName)
                .findFirst()
                .orElse("Unknown");
    }

    // --- DTOs ---
    record GreedyResponse(
            String message,
            String adopterId,
            String adopterName,
            List<AssignedDog> assignedDogs,
            double totalScore,
            double totalCost
    ) {}

    record AssignedDog(
            String dogId,
            String dogName,
            double cost
    ) {}

    record BacktrackingResponse(
            String message,
            String adopterId,
            String adopterName,
            List<List<AssignedDog>> combinations
    ) {}
}