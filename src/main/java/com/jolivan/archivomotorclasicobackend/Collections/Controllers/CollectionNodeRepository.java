package com.jolivan.archivomotorclasicobackend.Collections.Controllers;

import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionNode;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface CollectionNodeRepository extends Neo4jRepository<CollectionNode, Long> {

    @Query("MATCH (u:User{name: $username})<-[r:CreatedBy]-(c:CollectionNode) " +
//            "MATCH (c:CollectionNode) " +
//            "WHERE (c:CollectionNode)-[:CreatedBy]->(u) " +
            "RETURN c, collect(r), collect(u)")

//    @Query ("MATCH (c:CollectionNode)-[r:CreatedBy]->(u:User{name: $username})" +
//            "RETURN u,collect(r),collect(c)")
    List<CollectionNode> findByUsername(String username);
}
