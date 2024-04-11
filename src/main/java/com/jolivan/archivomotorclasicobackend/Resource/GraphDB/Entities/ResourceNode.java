package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities;

import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;
import java.util.Collection;

/**
 * This class represents the ResourceNode entity in the Neo4j database.
 * It is used to store the information of a resource in the database.
 */
@Data
@Node("ResourceNode")
public class ResourceNode {
    /**
     * The intern id of a node in the GraphDB.
     */
    @Id
    @GeneratedValue
    Long id;

    /**
     * The id of the resource in the system.
     */
    @Property("nodeId")
    String resourceID;

    String title;
    String competition;
    ZonedDateTime date;

    @Relationship(type = "CreatedBy")
    UserNode creator;

    @Relationship(type = "BelongsTo")
    MagazineIssueNode magazineIssue;

    @Relationship(type = "Starring")
    Collection<PersonNode> starring;

}
