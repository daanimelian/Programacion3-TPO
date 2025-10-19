package com.programacion3.adoptme.service;

import org.springframework.stereotype.Service;

import java.util.*;


/*
 Kruskal simple que opera sobre un subconjunto de nodos (p. ej. shelters/hubs)
 y aristas filtradas por tipo "NEAR". Devuelve lista de aristas del MST y costo total.
*/
@Service
public class MSTService {

    public static class Edge {
        public final String a;
        public final String b;
        public final double weight;
        public final String type; // p.ej. "NEAR"

        public Edge(String a, String b, double weight, String type) {
            this.a = a;
            this.b = b;
            this.weight = weight;
            this.type = type;
        }
    }

    public static class MSTResult {
        public final List<Edge> edges;
        public final double totalWeight;
        public MSTResult(List<Edge> edges, double totalWeight) {
            this.edges = edges; this.totalWeight = totalWeight;
        }
    }

    public MSTResult compute(Collection<String> nodesOfInterest, Collection<Edge> allEdges) {
        // Filtrar aristas NEAR que conecten nodos de interés
        List<Edge> edges = new ArrayList<>();
        for (Edge e : allEdges) {
            if (!"NEAR".equalsIgnoreCase(e.type)) continue;
            if (nodesOfInterest.contains(e.a) && nodesOfInterest.contains(e.b)) edges.add(e);
        }
        edges.sort(Comparator.comparingDouble(e -> e.weight));
        UnionFind uf = new UnionFind(nodesOfInterest);
        List<Edge> mst = new ArrayList<>();
        double total = 0.0;
        for (Edge e : edges) {
            if (uf.union(e.a, e.b)) {
                mst.add(e);
                total += e.weight;
            }
        }
        return new MSTResult(mst, total);
    }

    private static class UnionFind {
        private final Map<String, String> parent;
        private final Map<String, Integer> rank;

        UnionFind(Collection<String> nodes) {
            parent = new HashMap<>();
            rank = new HashMap<>();
            for (String n : nodes) { parent.put(n, n); rank.put(n, 0); }
        }

        String find(String x) {
            String p = parent.get(x);
            if (p == null) return x;
            if (!p.equals(x)) parent.put(x, find(p));
            return parent.get(x);
        }

        boolean union(String a, String b) {
            String ra = find(a), rb = find(b);
            if (ra.equals(rb)) return false;
            int rka = rank.getOrDefault(ra, 0), rkb = rank.getOrDefault(rb, 0);
            if (rka < rkb) parent.put(ra, rb);
            else if (rka > rkb) parent.put(rb, ra);
            else { parent.put(rb, ra); rank.put(ra, rka + 1); }
            return true;
        }
    }
}

