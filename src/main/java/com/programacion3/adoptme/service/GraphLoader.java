package com.programacion3.adoptme.service;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Carga la lista de adyacencias: para cada Shelter (por id),
 * qué vecinos tiene via relación :NEAR (aristas dirigidas).
 */
@Component
public class GraphLoader {

    private final Neo4jClient neo4j;

    public GraphLoader(Neo4jClient neo4j) {
        this.neo4j = neo4j;
    }

    public Map<String, List<String>> loadAdjacency() {
        String q = """
    MATCH (a:Shelter)-[r:NEAR]->(b:Shelter)
    RETURN a.id AS from, b.id AS to, r.distKm AS distKm, r.timeMin AS timeMin
""";


        Map<String, List<String>> g = new HashMap<>();
        neo4j.query(q).fetch().all().forEach(row -> {
            String from = (String) row.get("from");
            String to   = (String) row.get("to");
            g.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // si querés grafo no dirigido, también agregá la inversa:
            // g.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        });
        return g;
    }
}
