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
        neo4j.query("""
CREATE
      (d1:Dog {id:'D1', name:'Luna',  size:'SMALL',  weightKg:8,  age:2, energy:'LOW',    goodWithKids:true,  specialNeeds:false, priority:4}),
      (d2:Dog {id:'D2', name:'Toto',   size:'MEDIUM', weightKg:18, age:3, energy:'HIGH',   goodWithKids:true,  specialNeeds:false, priority:6}),
      (d3:Dog {id:'D3', name:'Rex', size:'LARGE',  weightKg:25, age:5, energy:'MEDIUM', goodWithKids:false, specialNeeds:true,  priority:8}),
      (d4:Dog {id:'D4', name:'Miranda',  size:'SMALL',  weightKg:10, age:1, energy:'HIGH',   goodWithKids:true,  specialNeeds:false, priority:5}),
      (d5:Dog {id:'D5', name:'Perchita',  size:'MEDIUM', weightKg:15, age:4, energy:'MEDIUM', goodWithKids:true,  specialNeeds:false, priority:3}),
      (d6:Dog {id:'D6', name:'Lina', size:'LARGE',  weightKg:30, age:6, energy:'LOW',    goodWithKids:false, specialNeeds:true,  priority:7})
""").run();
        
        neo4j.query("""
MATCH
      (a:Shelter {id:'A'}), (b:Shelter {id:'B'}), (c:Shelter {id:'C'}), (h:Shelter {id:'H'}),
      (d1:Dog {id:'D1'}), (d2:Dog {id:'D2'}), (d3:Dog {id:'D3'}), (d4:Dog {id:'D4'}), (d5:Dog {id:'D5'}), (d6:Dog {id:'D6'})
CREATE
      (a)-[:HAS_DOG]->(d1),
      (a)-[:HAS_DOG]->(d2),
      (b)-[:HAS_DOG]->(d3),
      (c)-[:HAS_DOG]->(d4),
      (h)-[:HAS_DOG]->(d5),
      (h)-[:HAS_DOG]->(d6)
""").run();
        
        neo4j.query("""
CREATE
      (p1:Adopter {id:'P1', name:'Camila', budget:25000, hasYard:true,  hasKids:true,  maxDogs:2}),
      (p2:Adopter {id:'P2', name:'Lucas', budget:18000, hasYard:false, hasKids:false, maxDogs:1}),
      (p3:Adopter {id:'P3', name:'Daniela',  budget:30000, hasYard:true,  hasKids:false, maxDogs:3})
""").run();
        
        neo4j.query("""
MATCH
      (p1:Adopter {id:'P1'}), (p2:Adopter {id:'P2'}), (p3:Adopter {id:'P3'}),
      (d1:Dog {id:'D1'}), (d2:Dog {id:'D2'}), (d3:Dog {id:'D3'}), (d4:Dog {id:'D4'}), (d5:Dog {id:'D5'})
CREATE
      (p1)-[:ADOPTS]->(d1),
      (p1)-[:ADOPTS]->(d2),
      (p2)-[:ADOPTS]->(d3),
      (p3)-[:ADOPTS]->(d4),
      (p3)-[:ADOPTS]->(d5)
""").run();
        
        System.out.println("[SEED] Base sembrada con shelters, dogs, adopters y relaciones NEAR.");
    }
}
