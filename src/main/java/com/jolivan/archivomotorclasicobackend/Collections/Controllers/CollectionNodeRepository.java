package com.jolivan.archivomotorclasicobackend.Collections.Controllers;

import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionNode;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface CollectionNodeRepository extends Neo4jRepository<CollectionNode, Long> {

    //TODO: Fix this query so it doesn't returns repeated collections
    @Query(
            "MATCH (u:User{name: $username})<-[x:CreatedBy]-(c:CollectionNode)-[y:Has]->(r:ResourceNode)" +
            "RETURN c AS CollectionNode, collect(x) as CreatedBy, collect(u) as UserNode, collect(y) as Has, collect(r) as ResourceNode " +
            "UNION " +
            "MATCH (u2:User{name: $username})<-[x2:CreatedBy]-(c2:CollectionNode) " +
            "RETURN c2 AS CollectionNode, collect(x2) as CreatedBy, collect(u2) as UserNode, collect([]) as Has, collect([]) as ResourceNode ")

//    @Query ("MATCH (c:CollectionNode)-[r:CreatedBy]->(u:User{name: $username})" +
//            "RETURN u,collect(r),collect(c)")
    List<CollectionNode> findByUsername(String username);
}
