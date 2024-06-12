package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Utils;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.*;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.PersonNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;

import java.util.ArrayList;
import java.util.List;

public class ResourceNodeToResource {

    private ResourceNodeToResource() {
        throw new IllegalStateException("Utility class");
    }
    public static Resource toResource(ResourceNode resourceNode){
        Resource resource = new Resource();
        resource.setId(resourceNode.getResourceID());
        resource.setTitle(resourceNode.getTitle());

        if(resourceNode.getCreator() != null) {
            resource.setCreator(resourceNode.getCreator().getName());
        }

        List<Property> properties = new ArrayList<>();

        if(resourceNode.getCompetition() != null) {
            Competition competition = new Competition();
            competition.setName(resourceNode.getCompetition());
            properties.add(competition);
        }


        if(resourceNode.getMagazineIssue() != null) {
            MagazineIssue magazineIssue = new MagazineIssue();
            magazineIssue.setName(resourceNode.getMagazineIssue().getName());
            magazineIssue.setTitle(resourceNode.getMagazineIssue().getTitle());
            magazineIssue.setNumber(resourceNode.getMagazineIssue().getNumber());
            magazineIssue.setDate(resourceNode.getMagazineIssue().getDate());
            magazineIssue.setCountry(resourceNode.getMagazineIssue().getCountry());
            properties.add(magazineIssue);
        }

        if(resourceNode.getPersons() != null){
            List<Person> personsList = new ArrayList<>();
            for (PersonNode personNode : resourceNode.getPersons()) {
                Person person = new Person();
                person.setName(personNode.getName());
                person.setAlias(personNode.getAlias());
                personsList.add(person);
            }
            Persons persons = new Persons();
            persons.setPersons(personsList);
            properties.add(persons);
        }

        Date date = new Date();
        if(resourceNode.getDate() != null){
            date.setDate(resourceNode.getDate());
            properties.add(date);
        }

        resource.setProperties(properties);

        return resource;
    }

    public static void completeResource(Resource resource, ResourceNode resourceNode) {
        resource.setId(resourceNode.getResourceID());
        resource.setTitle(resourceNode.getTitle());

        if(resourceNode.getCreator() != null) {
            resource.setCreator(resourceNode.getCreator().getName());
        }

        List<Property> properties = new ArrayList<>();

        if(resourceNode.getCompetition() != null) {
            Competition competition = new Competition();
            competition.setName(resourceNode.getCompetition());
            properties.add(competition);
        }


        if(resourceNode.getMagazineIssue() != null) {
            MagazineIssue magazineIssue = new MagazineIssue();
            magazineIssue.setName(resourceNode.getMagazineIssue().getName());
            magazineIssue.setTitle(resourceNode.getMagazineIssue().getTitle());
            magazineIssue.setNumber(resourceNode.getMagazineIssue().getNumber());
            magazineIssue.setDate(resourceNode.getMagazineIssue().getDate());
            magazineIssue.setCountry(resourceNode.getMagazineIssue().getCountry());
            properties.add(magazineIssue);
        }

        if(resourceNode.getPersons() != null){
            List<Person> personsList = new ArrayList<>();
            for (PersonNode personNode : resourceNode.getPersons()) {
                Person person = new Person();
                person.setName(personNode.getName());
                person.setAlias(personNode.getAlias());
                personsList.add(person);
            }
            Persons persons = new Persons();
            persons.setPersons(personsList);
            properties.add(persons);
        }

        Date date = new Date();
        if(resourceNode.getDate() != null){
            date.setDate(resourceNode.getDate());
            properties.add(date);
        }

        resource.setProperties(properties);
    }
}
