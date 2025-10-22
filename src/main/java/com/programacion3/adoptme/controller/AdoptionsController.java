package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.domain.Dog;
import com.programacion3.adoptme.exception.ResourceNotFoundException;
import com.programacion3.adoptme.repo.AdopterRepository;
import com.programacion3.adoptme.repo.DogRepository;
import com.programacion3.adoptme.service.ScorerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Backtracking - TODO: Implementar en Fase 2
     * Asigna perros considerando TODAS las restricciones posibles
     */
    @GetMapping("/constraints/backtracking")
    public ResponseEntity<String> backtrackingAdoption() {
        return ResponseEntity.ok("TODO: Backtracking adoption constraints");
    }

    // Métodos auxiliares
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
        // Costo base + extra por tamaño y necesidades especiales
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

    // DTOs
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
}