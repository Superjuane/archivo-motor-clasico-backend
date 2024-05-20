package com.jolivan.archivomotorclasicobackend.Resource.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ResourceForbiddenException;
import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ResourceNotFoundException;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
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
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.ResourceVectorDatabaseToResource;
import com.jolivan.archivomotorclasicobackend.Security.SecUtils.Session;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.UserNodeService;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Resource> getResources(Optional<String> page,
                                       Optional<String> size,
                                       Optional<String> title,
                                       Optional<String> description,
                                       Optional<String> place,
                                       Optional<List<ZonedDateTime>> dates,
                                       Optional<List<String>> competitions,
                                       Optional<List<String>> categories,
                                       Optional<List<String>> magazines,
                                       Optional<String> order) {
        List<Resource> result;

        if (title.isPresent()
                || description.isPresent()
                || place.isPresent()
                || dates.isPresent()
                || competitions.isPresent()
                || categories.isPresent()
                || magazines.isPresent()
        ) {
            result = getSomeResources(title.orElse(null),
                    description.orElse(null),
                    place.orElse(null),
                    dates.orElse(null),
                    competitions.orElse(null),
                    categories.orElse(null),
                    magazines.orElse(null)
            );
        } else if (page.isPresent() && size.isPresent()) {
            result = getResources(Integer.parseInt(page.get()), Integer.parseInt(size.get()));
        } else {
            result = getResources(0, 20);
        }


        return result;
    }

    private List<Resource> getSomeResources(String title, String description, String place, List<ZonedDateTime> dates, List<String> competitions, List<String> categories, List<String> magazines) {
        if((title != null && !title.isEmpty()) || (description != null && !description.isEmpty())){
            return searchInGraphAndInVector(title, description, place, dates, competitions, categories, magazines);
        }else{
            return searchOnlyInGraph(place, dates, competitions, categories, magazines);
        }
    }

    private List<Resource> searchInGraphAndInVector(String title, String description, String place, List<ZonedDateTime> dates, List<String> competitions, List<String> categories, List<String> magazines) {
//        List<Resource> resources = new ArrayList<>();
//
//        List<ResourceVectorDatabase> vectorQueryResponse = resourceVectorDatabaseService.searchResources(title, description);
//        List<String> ids = new ArrayList<>();
//
//        for (ResourceVectorDatabase r : vectorQueryResponse) {
//            ids.add(r.getID());
//        }
//
//        return resources;
        return null;
    }

    private List<Resource> searchOnlyInGraph(String place, List<ZonedDateTime> dates, List<String> competitions, List<String> categories, List<String> magazines) {
        List<Resource> resources = new ArrayList<>();

            List<ResourceNode> graphQueryResponse = resourceNodeService.searchResources(place, dates, competitions, magazines);

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

    public Resource getResourceByImageSimilarity(String image) {
        return blank();
    }

    public Resource insertResource(String creator, Resource newResource){

        //CHECK IF IMAGE ALREADY EXISTS
        String existingId = resourceVectorDatabaseService.isImageAlredyInDatabase(newResource.getImage());
        if(existingId != null){
            throw new ImageAlredyExistsException("Image already in database", existingId);
        }


        //INSERT INTO GRAPH DB

        UserNode user = userNodeService.getUserNodeByUsername(creator); //throws UserNodeNotFound
        ResourceNode newResourceGraph = new ResourceNode(newResource);
        ResourceNode queryResultG = resourceNodeService.addResourceNode(newResourceGraph);
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

    public List<String> getResourcesCompetitions() {
        return resourceNodeService.getResourcesCompetitions();
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

    public List<String> getResourcesMagazines() {
        return resourceNodeService.getResourcesMagazines();
    }

    public List<String> getResourcesMagazineIssues(Optional<String> magazine) {
       String magazineName = magazine.orElse(null);
         return resourceNodeService.getResourcesMagazineIssues(magazineName);
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
