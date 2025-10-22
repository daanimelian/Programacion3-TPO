package com.programacion3.adoptme.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.*;

@Node("Dog")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Dog {
    @Id
    private String id;
    private String name;
    private String size;       // SMALL, MEDIUM, LARGE
    private Integer weightKg;
    private Integer age;
    private String energy;     // LOW, MEDIUM, HIGH
    private Boolean goodWithKids;
    private Boolean specialNeeds;
    private Integer priority;  // Para priorización de adopción

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public Integer getWeight() {
        return weightKg;
    }

    public Integer getAge() {
        return age;
    }

    public String getEnergy() {
        return energy;
    }

    public Boolean getGoodWithKids() {
        return goodWithKids;
    }

    public Boolean getSpecialNeeds() {
        return specialNeeds;
    }

    public Integer getPriority() {
        return priority;
    }
}
