package com.programacion3.adoptme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.programacion3.adoptme.domain.Dog;
import com.programacion3.adoptme.repo.DogRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transport")
public class TransportController {
	
    private final DogRepository dogRepository;

    public TransportController(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    // /transport/optimal-dp (DP / Knapsack)
    @GetMapping("/optimal-dp")
    public ResponseEntity<TransportResponse> optimalTransport(@RequestParam(defaultValue = "40") int maxWeight) {
        List<Dog> dogs = dogRepository.findAll();
        int n = dogs.size();

        // Arrays para DP
        int[] weights = new int[n];
        int[] values = new int[n];
        String[] ids = new String[n];
        for (int i = 0; i < n; i++) {
            weights[i] = dogs.get(i).getWeight();
            values[i] = dogs.get(i).getPriority();
            ids[i] = dogs.get(i).getId();
        }

        // DP: dp[i][w] = max beneficio usando primeros i perros y peso w
        int[][] dp = new int[n + 1][maxWeight + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= maxWeight; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - weights[i - 1]] + values[i - 1]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Reconstruir solución óptima
        List<String> selectedDogs = new ArrayList<>();
        int w = maxWeight;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selectedDogs.add(ids[i - 1]);
                w -= weights[i - 1];
            }
        }

        return ResponseEntity.ok(new TransportResponse(selectedDogs, dp[n][maxWeight]));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TransportResponse {
        private List<String> selectedDogs;
        private int totalPriority;
    }
}
