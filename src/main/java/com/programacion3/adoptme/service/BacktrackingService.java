package com.programacion3.adoptme.service;

import com.programacion3.adoptme.domain.Adopter;
import com.programacion3.adoptme.domain.Dog;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BacktrackingService {

    @Getter
    public static class Result {
        private final List<Dog> assigned;
        private final double totalScore;
        private final double totalCost;

        public Result(List<Dog> assigned, double totalScore, double totalCost) {
            this.assigned = assigned;
            this.totalScore = totalScore;
            this.totalCost = totalCost;
        }
    }

    /**
     * Método público: asigna perros a un adoptante usando Backtracking
     */
    public Result assignDogs(Adopter adopter, List<Dog> candidates) {
        List<Dog> current = new ArrayList<>();
        List<Dog> best = new ArrayList<>();
        double[] bestScoreHolder = { -1.0 };

        backtrack(adopter, candidates, 0, current, 0.0, 0.0, best, bestScoreHolder);
        double totalCost = best.stream().mapToDouble(this::estimateCost).sum();
        double totalScore = bestScoreHolder[0];
        return new Result(best, totalScore, totalCost);
    }

    /**
     * Backtracking recursivo
     */
    private void backtrack(Adopter adopter, List<Dog> candidates, int index,
                           List<Dog> current, double currentCost, double currentScore,
                           List<Dog> best, double[] bestScoreHolder) {

        // Actualizar mejor resultado
        if (currentScore > bestScoreHolder[0]) {
            best.clear();
            best.addAll(current);
            bestScoreHolder[0] = currentScore;
        }

        if (index >= candidates.size()) return;

        Dog dog = candidates.get(index);

        // Evaluar restricciones para poder agregar
        boolean canAdd = true;
        if (adopter.getMaxDogs() != null && current.size() >= adopter.getMaxDogs()) canAdd = false;
        if (adopter.getBudget() != null && currentCost + estimateCost(dog) > adopter.getBudget()) canAdd = false;
        if (adopter.getHasYard() != null && !adopter.getHasYard() && "LARGE".equalsIgnoreCase(dog.getSize()))
            canAdd = false;
        if (adopter.getHasKids() != null && !adopter.getHasKids() && Boolean.TRUE.equals(dog.getGoodWithKids()))
            canAdd = false;

        // Opción 1: agregar perro
        if (canAdd) {
            current.add(dog);
            backtrack(adopter, candidates, index + 1, current,
                    currentCost + estimateCost(dog),
                    currentScore + scoreDog(dog, adopter),
                    best, bestScoreHolder);
            current.remove(current.size() - 1);
        }

        // Opción 2: no agregar perro
        backtrack(adopter, candidates, index + 1, current, currentCost, currentScore, best, bestScoreHolder);
    }

    /**
     * Calcula score de compatibilidad de un perro con adoptante
     */
    private double scoreDog(Dog dog, Adopter adopter) {
        double score = 0.0;
        if (Boolean.TRUE.equals(adopter.getHasKids()) && Boolean.TRUE.equals(dog.getGoodWithKids())) score += 3.0;
        if (Boolean.TRUE.equals(adopter.getHasYard()) && "LARGE".equalsIgnoreCase(dog.getSize())) score += 2.0;

        int energy = mapEnergy(dog.getEnergy());
        score += 2.0 * (1.0 - Math.abs(energy - 5) / 5.0); // energía moderada mejor

        int size = mapSize(dog.getSize());
        score += 1.0 * (3.0 - size) / 2.0; // preferencia neutra tamaño

        return score;
    }

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
        double specialNeedsCost = Boolean.TRUE.equals(dog.getSpecialNeeds()) ? 5000.0 : 0.0;
        return baseCost + sizeCost + specialNeedsCost;
    }
}
