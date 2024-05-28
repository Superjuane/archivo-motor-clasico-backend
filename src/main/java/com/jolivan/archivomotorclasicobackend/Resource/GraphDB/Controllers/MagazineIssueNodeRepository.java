package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.MagazineIssueNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MagazineIssueNodeRepository extends Neo4jRepository<MagazineIssueNode, Long> {

    @Query("MATCH (m:MagazineIssue) " +
            "WHERE m.name = $s " +
            "RETURN m")
    MagazineIssueNode getMagazineIssueNodeFromName(String s);

}
