package com.programacion3.adoptme.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathResponse {
    private boolean exists;   // Si hay un camino entre refugios
    private List<String> path; // Lista de IDs de refugios por los que pasa el camino
}
