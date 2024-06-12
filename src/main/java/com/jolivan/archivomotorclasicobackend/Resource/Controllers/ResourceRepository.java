package com.jolivan.archivomotorclasicobackend.Resource.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ResourceForbiddenException;
import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ResourceNotFoundException;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.*;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Date;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceImageSimilaritySearchResponseDTO;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceRequestDTO;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceUpdateDTO;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ResourceNodeService;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ExceptionControl.ResourceNodeNotFoundException;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Utils.ResourceNodeToResource;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions.ResourceVectorDatabaseNotUpdatedException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ResourceVectorDatabaseService;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabase;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions.IdIsNullException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions.ImageAlredyExistsException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabaseImageSearchListsResponse;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.ResourceVectorDatabaseToResource;
import com.jolivan.archivomotorclasicobackend.Security.SecUtils.Session;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.UserNodeService;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

import static com.jolivan.archivomotorclasicobackend.Utils.LoggingUtils.Log;
import static com.jolivan.archivomotorclasicobackend.Utils.LoggingUtils.LogError;

@Component
public class ResourceRepository {
    ResourceVectorDatabaseService resourceVectorDatabaseService;

    ResourceNodeService resourceNodeService;
    UserNodeService userNodeService;

    @Autowired
    public ResourceRepository(ResourceVectorDatabaseService resourceVectorDatabaseService, ResourceNodeService resourceNodeService, UserNodeService userNodeService){
        this.resourceVectorDatabaseService = resourceVectorDatabaseService;
        this.resourceNodeService = resourceNodeService;
        this.userNodeService = userNodeService;
    }

    public Resource getResource(String id){
        if(id == null) throw new IdIsNullException("Id is null");

        Resource resource;

        //GRAPH DATABASE DATA
        try {
            ResourceNode graphQueryResponse = resourceNodeService.getResourceNodeById(id);
            resource = ResourceNodeToResource.toResource(graphQueryResponse);
        } catch (ResourceNodeNotFoundException e) {
            throw new ResourceNotFoundException();
        }

        //VECTOR DATABASE DATA
        ResourceVectorDatabase vectorQueryResponse = resourceVectorDatabaseService.getResource(id);
        resource = ResourceVectorDatabaseToResource.completeResource(resource, vectorQueryResponse);


        return resource;
    }

    public Resource blank() {
        Resource resource = new Resource();
        resource.setId("-1");
        resource.setImageUrl("https://www.udacity.com/blog/wp-content/uploads/2021/02/img8.png");
        resource.setTitle("NoTitle");
        resource.setDescription("NoDescription");
        return resource;
    }

    public List<Resource> getResources(String page, String size, String title, String description, List<ZonedDateTime> dates, String competition, String magazine, Integer number, List<String> persons, String order) {

        List<Resource> result;

        if (title != null
                || description != null
                || dates != null
                || competition != null
                || magazine != null
                || number != null
                || persons != null
                || order != null
        ) {
            result = getSomeResources(title,
                    description,
                    dates,
                    competition,
                    magazine,
                    number,
                    persons,
                    order
            );
        } else if (page != null && size != null) {
            result = getResources(Integer.parseInt(page), Integer.parseInt(size));
        } else {
            result = getResources(0, 20);
        }

        return result;
    }

    private List<Resource> getSomeResources(String title, String description, List<ZonedDateTime> dates, String competition, String magazine, Integer number, List<String> persons, String order) {
        if((title != null && !title.isEmpty()) || (description != null && !description.isEmpty())){
            return searchInGraphAndInVector(title, description, dates, competition, magazine, number, persons, order);
        }else{
            return searchOnlyInGraph(dates, competition, magazine, number, persons, order);
        }
    }

    public static boolean isBetween(ZonedDateTime dateTimeToCheck, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        if(startDateTime == null || endDateTime == null) return true;
        return !dateTimeToCheck.isBefore(startDateTime) && !dateTimeToCheck.isAfter(endDateTime);
    }

    private List<Resource> searchInGraphAndInVector(String title, String description, List<ZonedDateTime> dates, String competition, String magazine, Integer number, List<String> persons, String order) {
        List<Resource> resources = new ArrayList<>();

        ZonedDateTime firstDate = null, lastDate = null;
        if(dates != null && !dates.isEmpty()){
            firstDate = dates.get(0);
            lastDate = dates.get(0);
            if(dates.size() > 1) {
                for (int i = 1; i < dates.size(); i++) {
                    if (dates.get(i).isBefore(firstDate))
                        firstDate = dates.get(i);
                    else if (dates.get(i).isAfter(lastDate))
                        lastDate = dates.get(i);
                }
            }
        }

        List<ResourceVectorDatabase> vectorQueryResponse = resourceVectorDatabaseService.searchResources(title, description);
        for(ResourceVectorDatabase r : vectorQueryResponse){
            Resource resource = ResourceVectorDatabaseToResource.toResource(r);
            if(dates != null || competition != null || magazine != null || number != null || persons != null) {
                ResourceNode graphQueryResponse = resourceNodeService.getResourceNodeById(r.getID());
                ResourceNodeToResource.completeResource(resource, graphQueryResponse);

                boolean add = true;
                if (!resource.getProperties().isEmpty()) {
                    for (Property p : resource.getProperties()) {
                        if(dates != null && p instanceof Date){
                            if(!isBetween(((Date) p).getDate(), firstDate, lastDate)){
                                add = false;
                            }
                        }
                        if(competition != null && p instanceof Competition){
                            if(!((((Competition) p).getName()).equals(competition))){
                                add = false;
                            }
                        }
                        if(magazine != null && p instanceof MagazineIssue){
                            if(!(((MagazineIssue) p).getTitle()).equals(magazine)){
                                add = false;
                            } else {
                                if(number != null && ((MagazineIssue) p).getNumber() != number){
                                    add = false;
                                }
                            }
                        }
                        if(persons != null && p instanceof Persons){
                            List<String> personsNames = new ArrayList<>();
                            for(Person person : ((Persons) p).getPersons()){
                                personsNames.add(person.getName());
                            }
                            for(String person : persons){
                                if(!personsNames.contains(person)){
                                    add = false;
                                }
                            }
                        }
                    }

                    //TODO: Fix this mess, it's just temporary
                    Map<String, Boolean> contains = new HashMap<>();
                    for (Property p : resource.getProperties()){
                        if(p instanceof Competition){
                            contains.put("competition", true);
                        }
                        if(p instanceof Date){
                            contains.put("date", true);
                        }
                        if(p instanceof MagazineIssue){
                            contains.put("magazineTitle", true);
                            if(number != null){
                                contains.put("magazineNumber", true);
                            }
                        }
                        if(p instanceof Persons){
                            contains.put("persons", true);
                        }
                    }
                    if(competition != null && !contains.containsKey("competition")){
                        add = false;
                    }
                    if(dates != null && !contains.containsKey("date")){
                        add = false;
                    }
                    if(magazine != null && !contains.containsKey("magazineTitle")){
                        add = false;
                    }
                    if(number != null && !contains.containsKey("magazineNumber")){
                        add = false;
                    }
                    if(persons != null && !contains.containsKey("persons")){
                        add = false;
                    }
                }



                if(add) resources.add(resource);
            }
            else{
                resources.add(resource);
            }
        }

        return resources;
    }


    private List<Resource> searchOnlyInGraph(List<ZonedDateTime> dates, String competition, String magazine, Integer number, List<String> persons, String order) {
        List<Resource> resources = new ArrayList<>();

            List<ResourceNode> graphQueryResponse = resourceNodeService.searchResources(dates, competition, magazine, number, persons, order);

            for(ResourceNode r : graphQueryResponse){
                Resource resource = ResourceNodeToResource.toResource(r);

                try {
                    ResourceVectorDatabase vectorQueryResponse = resourceVectorDatabaseService.getResource(r.getResourceID());
                    ResourceVectorDatabaseToResource.completeResource(resource, vectorQueryResponse);
                } catch (IdIsNullException e) {
                    LogError("Error getting ResourceVectorDatabase {id:"+ r.getResourceID() +"}...");
                }
                resources.add(resource);
            }
        return resources;
    }

    private List<Resource> getResources(int page, int size) {
        List<Resource> resources = new ArrayList<>();

        List<ResourceNode> graphQueryResponse = resourceNodeService.getAllResourceNodes();

        for(ResourceNode r : graphQueryResponse){
              Resource resource = ResourceNodeToResource.toResource(r);

            try {
                ResourceVectorDatabase vectorQueryResponse = resourceVectorDatabaseService.getResource(r.getResourceID());
                ResourceVectorDatabaseToResource.completeResource(resource, vectorQueryResponse);
            } catch (IdIsNullException e) {
                LogError("Error getting ResourceVectorDatabase {id:"+ r.getResourceID() +"}...");
            }
            resources.add(resource);
        }
        return resources;
    }

    public ResourceImageSimilaritySearchResponseDTO getResourceByImageSimilarity(String image, int limit) {
        List<Resource> similar = new ArrayList<>(), lessSimilar = new ArrayList<>();

        ResourceVectorDatabaseImageSearchListsResponse vectorQueryResponse = resourceVectorDatabaseService.searchResourcesByImage(image, limit);

        List<ResourceVectorDatabase> similarVectorResponse = vectorQueryResponse.getSimilar() , lessSimilarVectorResponse = vectorQueryResponse.getLessSimilar();
        for(ResourceVectorDatabase r : similarVectorResponse){
            Resource resource = ResourceVectorDatabaseToResource.toResource(r);

            ResourceNode graphQueryResponse = resourceNodeService.getResourceNodeById(r.getID());
            ResourceNodeToResource.completeResource(resource, graphQueryResponse);

            similar.add(resource);
        }

        for(ResourceVectorDatabase r : lessSimilarVectorResponse){
            Resource resource = ResourceVectorDatabaseToResource.toResource(r);

            ResourceNode graphQueryResponse = resourceNodeService.getResourceNodeById(r.getID());
            ResourceNodeToResource.completeResource(resource, graphQueryResponse);

            lessSimilar.add(resource);
        }

        return new ResourceImageSimilaritySearchResponseDTO(similar, lessSimilar);

    }

    public Resource insertResource(String creator, Resource newResource, ResourceRequestDTO resourceRequestDTO){

        //CHECK IF IMAGE ALREADY EXISTS
        String existingId = resourceVectorDatabaseService.isImageAlredyInDatabase(newResource.getImage());
        if(existingId != null){
            throw new ImageAlredyExistsException("Image already in database", existingId);
        }


        //INSERT INTO GRAPH DB

        UserNode user = userNodeService.getUserNodeByUsername(creator); //throws UserNodeNotFound
        ResourceNode queryResultG = resourceNodeService.addResourceNode(resourceRequestDTO);
        resourceNodeService.joinResourceToUser(queryResultG.getId(), user.getName());

        //INSERT INTO VECTOR DB
        ResourceVectorDatabase newResourceVector = new ResourceVectorDatabase(newResource);
        ResourceVectorDatabase queryResultV = resourceVectorDatabaseService.addResource(newResourceVector);

        //UPDATE GRAPH DB node with VectorDB.ID
//        queryResultG.setResourceID(queryResultV.getID());
//        resourceNodeService.updateResourceNode(queryResultG);
        ResourceNode GraphResourceUpdateResult = resourceNodeService.updateResourceNodeId(queryResultG.getId(), queryResultV.getID());


        Resource result = ResourceNodeToResource.toResource(GraphResourceUpdateResult);
        ResourceVectorDatabaseToResource.completeResource(result, queryResultV);

        return result;
    }

    public List<String> getResourcesIds(){
        return resourceVectorDatabaseService.getResourcesIds();
    }

    public List<String> getResourcesCompetitions(String competition) {
        List<String> competitions = resourceNodeService.getResourcesCompetitions();
        List<String> filtered = competitions;
        if(competition != null) filtered = competitions.stream().filter(c -> c.contains(competition)).toList();
        return filtered;
    }


    public Boolean deleteResource(String requestId) throws ResourceNodeNotFoundException {
        ResourceNode graphResult = resourceNodeService.getResourceNodeById(requestId);
        if(!graphResult.getCreator().getName().equals(Session.getCurrentUserName())){
            throw new ResourceForbiddenException("Resource not created by "+Session.getCurrentUserName());
        }

        Boolean graphResultOK = resourceNodeService.deleteResourceNode(requestId);
        Boolean vectorResultOK = resourceVectorDatabaseService.deleteResource(requestId);

        return graphResultOK && vectorResultOK;
    }

    public List<String> getResourcesMagazinesNames(String magazine) {
        List<String> magazines = resourceNodeService.getResourcesMagazinesNames();
        List<String> filtered = magazines;
        if(magazine != null) filtered = magazines.stream().filter(c -> c.toLowerCase().contains(magazine.toLowerCase())).toList();
        return filtered;
    }

    public List<Integer> getResourcesMagazinesNumbers(String magazineName) {
        List<Integer> magazinesNumbers = resourceNodeService.getResourcesMagazinesNumbers(magazineName);
//        List<int> filtered = magazinesNumbers;
//        if(magazineName != null) filtered = magazines.stream().filter(c -> c.contains(magazine)).toList();
        return magazinesNumbers;
    }

    public List<String> getResourcesMagazineIssues(Optional<String> magazine) {
       String magazineName = magazine.orElse(null);
         return resourceNodeService.getResourcesMagazineIssues(magazineName);
    }

    public List<String> getResourcesPersons(String person) {
        List<String> persons = resourceNodeService.getResourcesPersons();
        List<String> filtered = persons;
        if(person != null) filtered = persons.stream().filter(c -> c.contains(person)).toList();
        return filtered;
    }

    public List<Resource> getUserResources(String username) {
        List<Resource> resources = new ArrayList<>();

        List<ResourceNode> graphQueryResponse = resourceNodeService.searchResourcesByUser(username);

        for(ResourceNode r : graphQueryResponse){
            Resource resource = ResourceNodeToResource.toResource(r);

            try {
                ResourceVectorDatabase vectorQueryResponse = resourceVectorDatabaseService.getResource(r.getResourceID());
                ResourceVectorDatabaseToResource.completeResource(resource, vectorQueryResponse);
            } catch (IdIsNullException e) {
                LogError("Error getting ResourceVectorDatabase {id:"+ r.getResourceID() +"}...");
            }
            resources.add(resource);
        }
        return resources;
    }

    public Resource updateResource(String id, ResourceUpdateDTO resourceUpdateDTO) {
        ResourceNode updatingResourceNode = null;
        ResourceVectorDatabase updatingResourceVector = null;

//        if(!updatingResource.getCreator().equals(Session.getCurrentUserName())){
//            throw new ResourceForbiddenException("Not allowed to update a resource with a different creator");
//        }

        String username = Session.getCurrentUserName();

        if(id == null){
            throw new IdIsNullException("Id is null");
        }

        try{
            updatingResourceNode = resourceNodeService.updateResourceNode(id, resourceUpdateDTO);
            updatingResourceVector = resourceVectorDatabaseService.updateResource(id, resourceUpdateDTO);
        }catch (ResourceNodeNotFoundException | ResourceVectorDatabaseNotUpdatedException e) {
            throw new ResourceNotFoundException();
        }

        Resource result = ResourceNodeToResource.toResource(updatingResourceNode);
        ResourceVectorDatabaseToResource.completeResource(result, updatingResourceVector);

        return result;
    }

}
