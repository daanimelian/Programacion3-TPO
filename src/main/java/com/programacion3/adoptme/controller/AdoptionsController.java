package com.programacion3.adoptme.controller;

import com.programacion3.adoptme.domain.Dog;

import com.programacion3.adoptme.exception.ResourceNotFoundException;
import com.programacion3.adoptme.repo.AdopterRepository;
import com.programacion3.adoptme.repo.DogRepository;
import com.programacion3.adoptme.service.BacktrackingService;
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
    private final BacktrackingService backtrackingService;

    /**
     * Algoritmo Greedy: Asigna perros a un adoptante maximizando el score
     * Considera: presupuesto, jardín, niños, energía del perro
     * GET /adoptions/greedy?adopterId=P1
     */
    @GetMapping("/greedy")
    public ResponseEntity<AdoptionResponse> greedyAdoption(@RequestParam String adopterId) {
        var adopter = adopterRepository.findById(adopterId)
                .orElseThrow(() -> new ResourceNotFoundException("Adopter not found: " + adopterId));

        List<Dog> allDogs = dogRepository.findAll();

        if (allDogs.isEmpty()) {
            return ResponseEntity.ok(new AdoptionResponse(
                    "No dogs available for adoption",
                    adopterId,
                    adopter.getName(),
                    List.of(),
                    0.0,
                    0.0
            ));
        }

        List<ScorerService.Dog> candidates = allDogs.stream()
                .map(d -> new ScorerService.Dog(
                        d.getId(),
                        d.getGoodWithKids() != null && d.getGoodWithKids(),
                        "LARGE".equalsIgnoreCase(d.getSize()),
                        mapEnergy(d.getEnergy()),
                        mapSize(d.getSize()),
                        estimateCost(d)
                ))
                .collect(Collectors.toList());

        var result = scorerService.scoreAndAssign(
                candidates,
                adopter.getHasKids() != null && adopter.getHasKids(),
                adopter.getHasYard() != null && adopter.getHasYard(),
                adopter.getMaxDogs() != null ? adopter.getMaxDogs() : 1,
                adopter.getBudget() != null ? adopter.getBudget() : 20000.0
        );

        List<AssignedDog> assigned = result.assigned.stream()
                .map(d -> new AssignedDog(d.id, findDogName(allDogs, d.id), d.cost))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AdoptionResponse(
                "Greedy algorithm executed successfully",
                adopterId,
                adopter.getName(),
                assigned,
                result.totalScore,
                result.totalCost
        ));
    }

    /**
     * Backtracking: Asigna perros considerando TODAS las restricciones posibles
     * GET /adoptions/constraints/backtracking?adopterId=P1
     */
    @GetMapping("/constraints/backtracking")
    public ResponseEntity<AdoptionResponse> backtrackingAdoption(@RequestParam String adopterId) {
        var adopter = adopterRepository.findById(adopterId)
                .orElseThrow(() -> new ResourceNotFoundException("Adopter not found: " + adopterId));

        List<Dog> allDogs = dogRepository.findAll();

        var result = backtrackingService.assignDogs(adopter, allDogs);

        List<AssignedDog> assigned = result.getAssigned().stream()
                .map(d -> new AssignedDog(d.getId(), d.getName(), estimateCost(d)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AdoptionResponse(
                "Backtracking algorithm executed successfully",
                adopterId,
                adopter.getName(),
                assigned,
                result.getTotalScore(),
                result.getTotalCost()
        ));
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
    record AdoptionResponse(
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
