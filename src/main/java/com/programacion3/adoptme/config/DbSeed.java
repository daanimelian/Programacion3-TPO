package com.programacion3.adoptme.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.neo4j.core.Neo4jClient;

@Configuration
public class DbSeed {

    @Bean
    CommandLineRunner seed(Neo4jClient neo4j) {
        return args -> seedIfEmpty(neo4j);
    }

    @Transactional
    void seedIfEmpty(Neo4jClient neo4j) {
        // ¿ya hay shelters?
        long count = neo4j.query("MATCH (s:Shelter) RETURN count(s) AS c")
                .fetchAs(Long.class)
                .mappedBy((t,r) -> r.get("c").asLong())
                .one()
                .orElse(0L);

        if (count > 0) {
            System.out.println("[SEED] Ya existen shelters (" + count + "), no siembro.");
            return;
        }

        neo4j.query("MATCH (n) DETACH DELETE n").run();

        neo4j.query("""
CREATE (a:Shelter {id:'A', name:'Refugio A', capacity:20}),
       (b:Shelter {id:'B', name:'Refugio B', capacity:15}),
       (c:Shelter {id:'C', name:'Refugio C', capacity:10})
""").run();

        neo4j.query("""
MATCH (a:Shelter {id:'A'}),(b:Shelter {id:'B'}),(c:Shelter {id:'C'})
CREATE (a)-[:NEAR {distKm:12, timeMin:25}]->(b),
       (b)-[:NEAR {distKm:10, timeMin:18}]->(c),
       (a)-[:NEAR {distKm:30, timeMin:60}]->(c)
""").run();

        System.out.println("[SEED] Base sembrada con A, B, C y relaciones NEAR.");
    }
}
