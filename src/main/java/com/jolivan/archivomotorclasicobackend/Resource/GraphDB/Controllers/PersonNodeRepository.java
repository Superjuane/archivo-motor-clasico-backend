package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.MagazineIssueNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.PersonNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonNodeRepository extends Neo4jRepository<PersonNode, Long> {

    @Query("MATCH (p:Person) " +
            "WHERE p.name = $s " +
            "RETURN p")
    PersonNode getPersonNodeFromName(String s);

}
