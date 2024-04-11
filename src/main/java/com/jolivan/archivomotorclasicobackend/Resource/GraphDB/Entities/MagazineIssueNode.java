package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.time.ZonedDateTime;

@Data
@Node("MagazineIssue")
public class MagazineIssueNode {
    @Id
    @GeneratedValue
    Long id;

    String name;
    String title;
    Integer number;
    ZonedDateTime date;
    String country;
}
