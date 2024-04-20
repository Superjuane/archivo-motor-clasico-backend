package com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers;

import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface UserNodeRepository extends Neo4jRepository<UserNode, Long> {
    @Query("MATCH (u:User) WHERE u.name = $username RETURN u")
    UserNode findByUsername(String username);
}
