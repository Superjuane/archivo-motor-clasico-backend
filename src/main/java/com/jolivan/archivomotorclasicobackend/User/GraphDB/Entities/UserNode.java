package com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;
import java.util.Collection;

@Data
@Node("User")
public class UserNode {

        @Id
        @GeneratedValue
        Long id;

        @Property("name")
        String name;

        @Relationship("CreatedBy")
        Collection<ResourceNode> resources;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
