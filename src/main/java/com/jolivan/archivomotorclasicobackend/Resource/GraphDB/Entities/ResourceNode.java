package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.*;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.ZonedDateTime;
import java.util.Collection;

/**
 * This class represents the ResourceNode entity in the Neo4j database.
 * It is used to store the information of a resource in the database.
 */
@Data
@NoArgsConstructor
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
    String state;

    @Relationship(type = "CreatedBy", direction = Relationship.Direction.OUTGOING)
    UserNode creator;

    @Relationship(type = "BelongsTo", direction = Relationship.Direction.OUTGOING)
    MagazineIssueNode magazineIssue;

    @Relationship(type = "Starring", direction = Relationship.Direction.OUTGOING)
    Collection<PersonNode> starring;

    public ResourceNode(Resource resource) {
        this.resourceID = resource.getId() != null ? resource.getId() : null;
        this.title = resource.getTitle() != null ? resource.getTitle() : null;

        Collection<com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Property> properties = resource.getProperties();
        if(properties != null && !properties.isEmpty()){
            for (com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Property property : properties) {
                if (property instanceof Competition) {
                    this.competition = ((Competition) property).getName();
                } else if (property instanceof Date) {
                    this.date = ((Date) property).getDate();
    //            } else if (property instanceof MagazineIssue){
    //
    //            }
                }
            }
        }

        this.state = resource.getState() != null? resource.getState() : null;
    }
}
