package com.jolivan.archivomotorclasicobackend.Collections.Entities;

import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.PersonNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Collection;

@Data
@NoArgsConstructor
@Node("CollectionNode")
public class CollectionNode {
    /**
     * The intern id of a node in the GraphDB.
     */
    @Id
    @GeneratedValue
    Long id;

    String title;

    @Relationship(type = "CreatedBy", direction = Relationship.Direction.OUTGOING)
    UserNode creator;

    @Relationship(type = "Has", direction = Relationship.Direction.OUTGOING)
    Collection<ResourceNode> resources;
}
