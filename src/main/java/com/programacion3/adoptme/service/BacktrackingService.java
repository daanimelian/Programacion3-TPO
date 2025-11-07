package com.programacion3.adoptme.service;

import com.programacion3.adoptme.domain.Dog;
import com.programacion3.adoptme.domain.Adopter;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Servicio que implementa Backtracking para asignar perros a adoptantes
 * con restricciones de compatibilidad, presupuesto y capacidad.
 *
 * Problema: Asignar N perros a M adoptantes de forma que:
 * - Cada perro se asigna a máximo un adoptante
 * - Se respetan restricciones de presupuesto, capacidad y compatibilidad
 * - Se maximiza la satisfacción total
 */
@Service
public class BacktrackingService {

    /**
     * Asignación: adopterId -> lista de dogIds
     */
    public static class Assignment {
        public final Map<String, List<String>> assignments; // adopterId -> [dogIds]
        public final double totalScore;

        public Assignment(Map<String, List<String>> assignments, double totalScore) {
            this.assignments = assignments;
            this.totalScore = totalScore;
        }
    }

    // ==================== Métodos Helper ====================

    /**
     * Determina si un perro necesita jardín basándose en su tamaño
     */
    private boolean needsGarden(Dog dog) {
        return "LARGE".equalsIgnoreCase(dog.getSize());
    }

    /**
     * Calcula el costo estimado de adopción de un perro
     */
    private double estimateCost(Dog dog) {
        double baseCost = 5000.0;
        double sizeCost = mapSize(dog.getSize()) * 2000.0;
        double specialNeedsCost = (dog.getSpecialNeeds() != null && dog.getSpecialNeeds()) ? 5000.0 : 0.0;
        return baseCost + sizeCost + specialNeedsCost;
    }

    /**
     * Convierte el nivel de energía de String a int (1-10)
     */
    private int mapEnergyToInt(String energy) {
        if (energy == null) return 5;
        return switch (energy.toUpperCase()) {
            case "LOW" -> 2;
            case "MEDIUM" -> 5;
            case "HIGH" -> 8;
            default -> 5;
        };
    }

    /**
     * Mapea el tamaño del perro a un valor numérico
     */
    private int mapSize(String size) {
        if (size == null) return 2;
        return switch (size.toUpperCase()) {
            case "SMALL" -> 1;
            case "MEDIUM" -> 2;
            case "LARGE" -> 3;
            default -> 2;
        };
    }

    /**
     * Encuentra la mejor asignación usando backtracking.
     *
     * @param dogs lista de perros disponibles
     * @param adopters lista de adoptantes
     * @return mejor asignación encontrada
     */
    public Assignment findBestAssignment(List<Dog> dogs, List<Adopter> adopters) {
        if (dogs.isEmpty() || adopters.isEmpty()) {
            return new Assignment(new HashMap<>(), 0.0);
        }

        // Estado inicial: ningún perro asignado
        Map<String, List<String>> currentAssignment = new HashMap<>();
        for (Adopter a : adopters) {
            currentAssignment.put(a.getId(), new ArrayList<>());
        }

        Map<String, Double> currentCost = new HashMap<>();
        for (Adopter a : adopters) {
            currentCost.put(a.getId(), 0.0);
        }

        // Variables para la mejor solución encontrada
        BestSolution best = new BestSolution();

        // Iniciar backtracking
        backtrack(0, dogs, adopters, currentAssignment, currentCost, 0.0, best);

        return new Assignment(best.assignments, best.score);
    }

    /**
     * Clase auxiliar para mantener la mejor solución
     */
    private static class BestSolution {
        Map<String, List<String>> assignments = new HashMap<>();
        double score = 0.0;
    }

    /**
     * Algoritmo de backtracking recursivo.
     *
     * @param dogIndex índice del perro actual a asignar
     * @param dogs lista de perros
     * @param adopters lista de adoptantes
     * @param currentAssignment asignación actual
     * @param currentCost costo acumulado por adoptante
     * @param currentScore score total actual
     * @param best mejor solución encontrada hasta ahora
     */
    private void backtrack(
            int dogIndex,
            List<Dog> dogs,
            List<Adopter> adopters,
            Map<String, List<String>> currentAssignment,
            Map<String, Double> currentCost,
            double currentScore,
            BestSolution best
    ) {
        // Caso base: todos los perros fueron considerados
        if (dogIndex == dogs.size()) {
            // Si esta solución es mejor, guardarla
            if (currentScore > best.score) {
                best.score = currentScore;
                best.assignments = deepCopy(currentAssignment);
            }
            return;
        }

        Dog dog = dogs.get(dogIndex);

        // Opción 1: No asignar este perro a nadie (puede quedar sin adoptar)
        backtrack(dogIndex + 1, dogs, adopters, currentAssignment, currentCost, currentScore, best);

        // Opción 2: Intentar asignar este perro a cada adoptante
        for (Adopter adopter : adopters) {
            // Verificar si es viable asignar este perro a este adoptante
            if (canAssign(dog, adopter, currentAssignment, currentCost)) {
                // Calcular score de esta asignación
                double score = calculateScore(dog, adopter);
                double dogCost = estimateCost(dog);

                // Hacer asignación (forward)
                currentAssignment.get(adopter.getId()).add(dog.getId());
                currentCost.put(adopter.getId(), currentCost.get(adopter.getId()) + dogCost);

                // Recursión
                backtrack(dogIndex + 1, dogs, adopters, currentAssignment, currentCost,
                        currentScore + score, best);

                // Deshacer asignación (backtrack)
                currentAssignment.get(adopter.getId()).remove(currentAssignment.get(adopter.getId()).size() - 1);
                currentCost.put(adopter.getId(), currentCost.get(adopter.getId()) - dogCost);
            }
        }
    }

    /**
     * Verifica si se puede asignar un perro a un adoptante respetando restricciones.
     */
    private boolean canAssign(
            Dog dog,
            Adopter adopter,
            Map<String, List<String>> currentAssignment,
            Map<String, Double> currentCost
    ) {
        int maxDogs = adopter.getMaxDogs() != null ? adopter.getMaxDogs() : 1;
        double budget = adopter.getBudget() != null ? adopter.getBudget() : 20000.0;
        boolean hasKids = adopter.getHasKids() != null && adopter.getHasKids();
        boolean hasYard = adopter.getHasYard() != null && adopter.getHasYard();

        boolean dogGoodWithKids = dog.getGoodWithKids() != null && dog.getGoodWithKids();
        double dogCost = estimateCost(dog);
        boolean dogNeedsGarden = needsGarden(dog);

        // Restricción 1: No exceder capacidad máxima de perros
        if (currentAssignment.get(adopter.getId()).size() >= maxDogs) {
            return false;
        }

        // Restricción 2: No exceder presupuesto
        if (currentCost.get(adopter.getId()) + dogCost > budget) {
            return false;
        }

        // Restricción 3: Si el perro no es bueno con niños y el adoptante tiene niños, no asignar
        if (hasKids && !dogGoodWithKids) {
            return false;
        }

        // Restricción 4: Si el perro necesita jardín y el adoptante no tiene, no asignar
        if (dogNeedsGarden && !hasYard) {
            return false;
        }

        return true;
    }

    /**
     * Calcula el score de asignar un perro específico a un adoptante específico.
     */
    private double calculateScore(Dog dog, Adopter adopter) {
        double score = 0.0;

        boolean hasKids = adopter.getHasKids() != null && adopter.getHasKids();
        boolean hasYard = adopter.getHasYard() != null && adopter.getHasYard();
        int preferredEnergy = adopter.getPreferredEnergy() != null ? adopter.getPreferredEnergy() : 5;

        boolean dogGoodWithKids = dog.getGoodWithKids() != null && dog.getGoodWithKids();
        boolean dogNeedsGarden = needsGarden(dog);
        int dogEnergy = mapEnergyToInt(dog.getEnergy());

        // +5 puntos si es compatible con niños y el adoptante tiene niños
        if (hasKids && dogGoodWithKids) {
            score += 5.0;
        }

        // +3 puntos si el perro necesita jardín y el adoptante tiene jardín
        if (dogNeedsGarden && hasYard) {
            score += 3.0;
        }

        // +0 a +5 puntos por compatibilidad de energía (menor diferencia = mejor)
        int energyDiff = Math.abs(dogEnergy - preferredEnergy);
        score += Math.max(0, 5.0 - energyDiff);

        return score;
    }

    /**
     * Crea una copia profunda del mapa de asignaciones.
     */
    private Map<String, List<String>> deepCopy(Map<String, List<String>> original) {
        Map<String, List<String>> copy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }
}
