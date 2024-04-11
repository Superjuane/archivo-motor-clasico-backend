package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Person")
public class PersonNode {
    @Id
    @GeneratedValue
    Long id;

    String name;
    String alias;
}
