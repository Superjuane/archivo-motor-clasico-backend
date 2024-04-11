package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Utils;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Competition;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Date;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.MagazineIssue;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Property;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;

import java.util.ArrayList;
import java.util.List;

public class ResourceNodeToResource {

    private ResourceNodeToResource() {
        throw new IllegalStateException("Utility class");
    }
    public static Resource toResource(ResourceNode resourceNode){
        Resource resource = new Resource();
        resource.setID(resourceNode.getResourceID());
        resource.setTitle(resourceNode.getTitle());

        List<Property> properties = new ArrayList<>();

        Competition competition = new Competition();
            competition.setName(resourceNode.getCompetition() != null? resourceNode.getCompetition() : null);
        properties.add(competition);

        MagazineIssue magazineIssue = new MagazineIssue();
        if(resourceNode.getMagazineIssue() != null) {
            magazineIssue.setName(resourceNode.getMagazineIssue().getName());
            magazineIssue.setTitle(resourceNode.getMagazineIssue().getTitle());
            magazineIssue.setNumber(resourceNode.getMagazineIssue().getNumber());
            magazineIssue.setDate(resourceNode.getMagazineIssue().getDate());
            magazineIssue.setCountry(resourceNode.getMagazineIssue().getCountry());
            properties.add(magazineIssue);
        }

        Date date = new Date();
        if(resourceNode.getDate() != null){
            date.setDate(resourceNode.getDate());
            properties.add(date);
        }

        resource.setProperties(properties);

        return resource;
    }

    public static void completeResource(Resource resource, ResourceNode graphQueryResponse) {
        List<Property> properties = new ArrayList<>();

        Competition competition = new Competition();
            competition.setName(graphQueryResponse.getCompetition());
        properties.add(competition);

        MagazineIssue magazineIssue = new MagazineIssue();
            magazineIssue.setName(graphQueryResponse.getMagazineIssue().getName());
            magazineIssue.setTitle(graphQueryResponse.getMagazineIssue().getTitle());
            magazineIssue.setNumber(graphQueryResponse.getMagazineIssue().getNumber());
            magazineIssue.setDate(graphQueryResponse.getMagazineIssue().getDate());
            magazineIssue.setCountry(graphQueryResponse.getMagazineIssue().getCountry());
        properties.add(magazineIssue);

        resource.setProperties(properties);
    }
}
