package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceNodeRepository extends Neo4jRepository<ResourceNode, Long> {

    //! OLD ONE, DO NOT USE
//    @Query("MATCH (resourceNode:ResourceNode) " +
//            "WHERE resourceNode.nodeId = $id " +
//            "RETURN resourceNode{" +
//            "        .competition," +
//            "        .date," +
//            "        .nodeId," +
//            "        .title," +
//            "        __nodeLabels__:" +
//            "            labels(resourceNode)," +
//            "            __internalNeo4jId__:" +
//            "                id(resourceNode)," +
//            "                __elementId__: id(resourceNode)," +
//            "    ResourceNode_CreatedBy_User: [" +
//            "        (resourceNode)-[:`CreatedBy`]->(resourceNode_creator:`User`) | resourceNode_creator{.name, __nodeLabels__: labels(resourceNode_creator), __internalNeo4jId__: id(resourceNode_creator), __elementId__: id(resourceNode_creator)}]," +
//            "    ResourceNode_Starring_Person: [" +
//            "        (resourceNode)-[:`Starring`]->(resourceNode_starring:`Person`) | resourceNode_starring{.alias, .name, __nodeLabels__: labels(resourceNode_starring), __internalNeo4jId__: id(resourceNode_starring), __elementId__: id(resourceNode_starring)}]," +
//            "    ResourceNode_BelongsTo_MagazineIssue: [" +
//            "        (resourceNode)-[:`BelongsTo`]->(resourceNode_magazineIssue:`MagazineIssue`) | resourceNode_magazineIssue{.country, .date, .name, .number, .title, __nodeLabels__: labels(resourceNode_magazineIssue), __internalNeo4jId__: id(resourceNode_magazineIssue), __elementId__: id(resourceNode_magazineIssue)}]}")

    //? OLD ONES, DOESN'T ACCEPT NULLS
//    @Query( "MATCH (m:MagazineIssue)<-[a:AppearsIn]-(resourceNode:ResourceNode{nodeId: $id})-[c:CreatedBy]->(u:User) " +
//            "RETURN resourceNode as resourceNode, collect(c) as CreatedBy, collect(u) as UserNode, collect(a) as AppearsIn, collect(m) as MagazineIssueNode"
//            )

    //? OLD ONES, DOESN'T ACCEPT NULLS
//    @Query( "MATCH (m:MagazineIssue)<-[a:AppearsIn]-(resourceNode:ResourceNode{nodeId: $id}) " +
//            "MATCH (resourceNode:ResourceNode{nodeId: $id})-[c:CreatedBy]->(u:User) " +
//            "MATCH (resourceNode:ResourceNode{nodeId: $id})-[s:Starring]->(p:Person)" +
//            "RETURN resourceNode as resourceNode, " +
//            "collect(a) as AppearsIn, collect(m) as MagazineIssueNode, " +
//            "collect(c) as CreatedBy, collect(u) as UserNode, " +
//            "collect(s) as Starring, collect(p) as Person"
//
//    )

    @Query("MATCH (resourceNode:ResourceNode{nodeId: $id}) " +
            "WITH resourceNode " +
            "OPTIONAL MATCH (resourceNode)-[c:CreatedBy]->(u:User) " +
            "OPTIONAL MATCH (resourceNode)-[s:Starring]->(p:Person) " +
            "OPTIONAL MATCH (resourceNode)-[a:AppearsIn]->(m:MagazineIssue) " +
            "RETURN resourceNode as resourceNode, " +
            "collect(a) as AppearsIn, collect(m) as MagazineIssueNode, " +
            "collect(c) as CreatedBy, collect(u) as UserNode, " +
            "collect(s) as Starring, collect(p) as Person"
    )
    public ResourceNode findById(String id);

//    @Query( "MATCH (resourceNode:ResourceNode) " +
//            "WHERE " +
////                "(resourceNode.place = $place or $place is null) " +
//                /*"AND */"(resourceNode.date >= datetime({year:$firstYear, month:$firstMonth, day:$firstDay}) or $firstYear = 0) " +
//                "AND (resourceNode.date <= datetime({year:$lastYear, month:$lastMonth, day:$lastDay}) or $lastYear = 9999) " +
//                "AND (resourceNode.competition IN $competitions OR $competitions is null) " +
////                "AND (resourceNode.magazines IN $magazines OR $magazines is null) " +
//            "RETURN resourceNode{" +
//            "        .competition," +
//            "        .date," +
//            "        .nodeId," +
//            "        .title," +
//            "        __nodeLabels__:" +
//            "            labels(resourceNode)," +
//            "            __internalNeo4jId__:" +
//            "                id(resourceNode)," +
//            "                __elementId__: id(resourceNode)," +
//            "    ResourceNode_CreatedBy_User: [" +
//            "        (resourceNode)-[:`CreatedBy`]->(resourceNode_creator:`User`) | resourceNode_creator{.name, __nodeLabels__: labels(resourceNode_creator), __internalNeo4jId__: id(resourceNode_creator), __elementId__: id(resourceNode_creator)}]," +
//            "    ResourceNode_Starring_Person: [" +
//            "        (resourceNode)-[:`Starring`]->(resourceNode_starring:`Person`) | resourceNode_starring{.alias, .name, __nodeLabels__: labels(resourceNode_starring), __internalNeo4jId__: id(resourceNode_starring), __elementId__: id(resourceNode_starring)}]," +
//            "    ResourceNode_BelongsTo_MagazineIssue: [" +
//            "        (resourceNode)-[:`BelongsTo`]->(resourceNode_magazineIssue:`MagazineIssue`) | resourceNode_magazineIssue{.country, .date, .name, .number, .title, __nodeLabels__: labels(resourceNode_magazineIssue), __internalNeo4jId__: id(resourceNode_magazineIssue), __elementId__: id(resourceNode_magazineIssue)}]} "
////            +"SKIP 1 "+
////            "LIMIT 1 "
//            )

    @Query( "MATCH (resourceNode:ResourceNode) " +
            "WHERE " +
                    "(resourceNode.date >= datetime({year:$firstYear, month:$firstMonth, day:$firstDay}) " +
                        "OR $firstYear = 0) " +
                "AND (resourceNode.date <= datetime({year:$lastYear, month:$lastMonth, day:$lastDay}) " +
                        "OR $lastYear = 999999) " +
                "AND (resourceNode.competition = $competition " +
                        "OR $competition is null) " +
            "WITH resourceNode " +
            "OPTIONAL MATCH (resourceNode)-[c:CreatedBy]->(u:User) " +
                "WITH resourceNode, c, u " +
            "OPTIONAL MATCH (resourceNode)-[s:Starring]->(p:Person) " +
            "WITH resourceNode, c, u, s, p " +
                "WHERE " +
                    "(p.name IN $persons " +
                        "OR $persons is null) " +
            "OPTIONAL MATCH (resourceNode)-[a:AppearsIn]->(m:MagazineIssue) " +
            "WITH resourceNode, c, u, s, p, a, m " +
                "WHERE " +
                    "($magazine is null AND $number is null) " +
                    "OR (m.title = $magazine AND $number is null) " +
                    "OR (m.title = $magazine AND m.number = $number) " +
            "RETURN resourceNode as resourceNode, " +
                    "collect(a) as AppearsIn, collect(m) as MagazineIssueNode, " +
                    "collect(c) as CreatedBy, collect(u) as UserNode, " +
                    "collect(s) as Starring, collect(p) as Person"

    )
    List<ResourceNode> searchResources(int firstYear, int firstMonth, int firstDay, int lastYear, int lastMonth, int lastDay, String competition, String magazine, Integer number, List<String> persons, String order);

    @Query("MATCH (r:ResourceNode) " +
            "WHERE r.nodeId = $id "+
            "DETACH DELETE r")
    void deleteById(String id); //TODO: make delete not return always an empty array. I don't know what to return and what to check to see if works

    @Query("MATCH (r:ResourceNode) " +
            "WHERE r.nodeId = $resourceId " +
            "MATCH (u:User) " +
            "WHERE u.name = $userId " +
            "CREATE (r)-[:CreatedBy]->(u)")
    void joinResourceToUser(String resourceId, String userId); //TODO: make joinResourceToUser not return always an empty array. I don't know what to return and what to check to see if works

    @Query("MATCH (r:ResourceNode) " +
            "WHERE id(r) = $id " +
            "MATCH (u:User) " +
            "WHERE u.name = $userId " +
            "CREATE (r)-[:CreatedBy]->(u)")
    void joinResourceToUser(Long id, String userId);

    @Query("MATCH (r:ResourceNode) " +
            "WITH DISTINCT r.competition AS competitions " +
            "RETURN competitions")
    List<String> getResourcesCompetitions();

@Query("MATCH (m:MagazineIssue) " +
        "WITH DISTINCT m.title AS magazines " +
        "RETURN magazines")
    List <String> getResourcesMagazinesNames();

    @Query("MATCH (m:MagazineIssue) " +
            "WHERE m.title = $magazineName " +
            "WITH DISTINCT m.number AS magazineNumbers " +
            "RETURN magazineNumbers")
    List <Integer> getResourcesMagazinesNumbers(String magazineName);

@Query("MATCH (m:MagazineIssue) " +
        "WHERE ((m.title = $magazineName) or ($magazineName is null)) " +
        "WITH DISTINCT m.name AS magazineIssues " +
        "RETURN magazineIssues")
    List<String> getResourcesMagazineIssues(String magazineName);

    @Query("MATCH (resourceNode:ResourceNode)-[:CreatedBy]->(u:User) " +
            "WHERE u.name = $username " +
            "RETURN resourceNode{" +
            "        .competition," +
            "        .date," +
            "        .nodeId," +
            "        .title," +
            "        __nodeLabels__:" +
            "            labels(resourceNode)," +
            "            __internalNeo4jId__:" +
            "                id(resourceNode)," +
            "                __elementId__: id(resourceNode)," +
            "    ResourceNode_CreatedBy_User: [" +
            "        (resourceNode)-[:`CreatedBy`]->(resourceNode_creator:`User`) | resourceNode_creator{.name, __nodeLabels__: labels(resourceNode_creator), __internalNeo4jId__: id(resourceNode_creator), __elementId__: id(resourceNode_creator)}]," +
            "    ResourceNode_Starring_Person: [" +
            "        (resourceNode)-[:`Starring`]->(resourceNode_starring:`Person`) | resourceNode_starring{.alias, .name, __nodeLabels__: labels(resourceNode_starring), __internalNeo4jId__: id(resourceNode_starring), __elementId__: id(resourceNode_starring)}]," +
            "    ResourceNode_BelongsTo_MagazineIssue: [" +
            "        (resourceNode)-[:`BelongsTo`]->(resourceNode_magazineIssue:`MagazineIssue`) | resourceNode_magazineIssue{.country, .date, .name, .number, .title, __nodeLabels__: labels(resourceNode_magazineIssue), __internalNeo4jId__: id(resourceNode_magazineIssue), __elementId__: id(resourceNode_magazineIssue)}]}")
    List<ResourceNode> findByUser(String username);

    @Query("MATCH (r:ResourceNode) " +
            "WHERE r.nodeId = $resourceId " +
            "MATCH (m:MagazineIssue) " +
            "WHERE m.name = $magazineIssueId " +
            "CREATE (r)-[:AppearsIn]->(m)")
    void joinResourceToMagazineIssue(String resourceId, String magazineIssueId);

    @Query("MATCH (p:Person) " +
            "WITH DISTINCT p.name AS persons " +
            "RETURN persons")
    List<String> getResourcesPersons();

}
