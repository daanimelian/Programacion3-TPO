package com.programacion3.adoptme.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.*;
import java.util.UUID;

@Node("Shelter")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Shelter {
    @Id
    @EqualsAndHashCode.Include
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    private String name;
    private Integer capacity;
}
