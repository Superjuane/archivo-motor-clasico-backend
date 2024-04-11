package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ResourceNodeRepository extends Neo4jRepository<ResourceNode, Long> {

    @Query("MATCH (resourceNode:ResourceNode) " +
            "WHERE resourceNode.nodeId = $id " +
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
    public ResourceNode findById(String id);

    @Query( "MATCH (resourceNode:ResourceNode) " +
            "WHERE " +
//                "(resourceNode.place = $place or $place is null) " +
                /*"AND */"(resourceNode.date >= datetime({year:$firstYear, month:$firstMonth, day:$firstDay}) or $firstYear is null) " +
                "AND (resourceNode.date <= datetime({year:$lastYear, month:$lastMonth, day:$lastDay}) or $lastYear is null) " +
                "AND (resourceNode.competition IN $competitions OR $competitions is null) " +
//                "AND (resourceNode.magazines IN $magazines OR $magazines is null) " +
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
            "        (resourceNode)-[:`BelongsTo`]->(resourceNode_magazineIssue:`MagazineIssue`) | resourceNode_magazineIssue{.country, .date, .name, .number, .title, __nodeLabels__: labels(resourceNode_magazineIssue), __internalNeo4jId__: id(resourceNode_magazineIssue), __elementId__: id(resourceNode_magazineIssue)}]}"
            )
    List<ResourceNode> searchResources(String place, int firstYear, int firstMonth, int firstDay, int lastYear, int lastMonth, int lastDay, List<String> competitions, List<String> magazines);

}
