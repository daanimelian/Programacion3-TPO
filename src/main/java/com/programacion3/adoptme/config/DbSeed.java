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
       (c:Shelter {id:'C', name:'Refugio C', capacity:10}),
       (h:Shelter {id:'H', name:'Central Hub', capacity:40})

""").run();

        neo4j.query("""
MATCH (a:Shelter {id:'A'}),(b:Shelter {id:'B'}),(c:Shelter {id:'C'}), (h:Shelter {id:'H'})
CREATE 
    (h)-[:NEAR {distKm:5,  timeMin:10}]->(a),
    (h)-[:NEAR {distKm:7,  timeMin:15}]->(b),
    (h)-[:NEAR {distKm:9,  timeMin:18}]->(c),
    (a)-[:NEAR {distKm:6,  timeMin:12}]->(b),
    (b)-[:NEAR {distKm:8,  timeMin:16}]->(c),
    (a)-[:NEAR {distKm:10, timeMin:20}]->(c),
    (c)-[:NEAR {distKm:14, timeMin:25}]->(h)
""").run();
        System.out.println("[SEED] Base sembrada con A, B, C y relaciones NEAR.");
    }
}
