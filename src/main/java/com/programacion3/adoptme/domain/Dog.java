package com.programacion3.adoptme.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.*;
import java.util.UUID;

@Node("Dog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dog {
    @Id
    @EqualsAndHashCode.Include
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String name;
    private String size;       // SMALL, MEDIUM, LARGE
    private Integer weightKg;
    private Integer age;
    private String energy;     // LOW, MEDIUM, HIGH
    private Boolean goodWithKids;
    private Boolean specialNeeds;
    private Integer priority;  // Para priorización de adopción
}
