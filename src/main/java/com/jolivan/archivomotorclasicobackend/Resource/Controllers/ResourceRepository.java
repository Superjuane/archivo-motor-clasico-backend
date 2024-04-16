package com.jolivan.archivomotorclasicobackend.Resource.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Property;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.*;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ResourceNodeService;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Utils.ResourceNodeToResource;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ResourceVectorDatabaseService;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabase;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.IdIsNullException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.ImageAlredyExistsException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.ResourceVectorDatabaseToResource;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.WeaviateResultConverter;
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

    @Autowired
    public ResourceRepository(ResourceVectorDatabaseService resourceVectorDatabaseService, ResourceNodeService resourceNodeService){
        this.resourceVectorDatabaseService = resourceVectorDatabaseService;
        this.resourceNodeService = resourceNodeService;
    }

    public Resource getResource(String id){
        if(id == null) throw new IdIsNullException("Id is null");

        //TODO: eliminate this developement shortcut
        if(id.equals("4")) id = "f4f31cb6-3ed1-45c9-8931-b73f2298d9c5";

        //PREPARING RESOURCE
        Resource resource = new Resource();
        resource.setID(id);
        resource.setTimestamp(ZonedDateTime.now());
        resource.setImageUrl("https://i.pinimg.com/236x/b3/df/16/b3df164ad9902eae7ae657def1bd4c71.jpg");


        //VECTOR DATABASE DATA

        ResourceVectorDatabase vectorQueryResponse = resourceVectorDatabaseService.getResource(id);
        resource = ResourceVectorDatabaseToResource.toResource(vectorQueryResponse);

        //GRAPH DATABASE DATA
        try {
            ResourceNode graphQueryResponse = resourceNodeService.getResourceNodeById(id);
            ResourceNodeToResource.completeResource(resource, graphQueryResponse);
        } catch (Exception e) {
            LogError("Error getting ResourceNode {id:"+ id +"}...");
        }

        return resource;
    }

    public Resource blank() {
        Resource resource = new Resource();
        resource.setID("-1");
        resource.setImageUrl("https://www.udacity.com/blog/wp-content/uploads/2021/02/img8.png");
        resource.setTitle("NoTitle");
        resource.setDescription("NoDescription");
        resource.setTimestamp(ZonedDateTime.now());
        return resource;
    }
/*
               @RequestParam(name="title") Optional<String> title,
                                @RequestParam(name="description") Optional<String> description,
                                @RequestParam(name="place") Optional<String> place,
                                @RequestParam(name="creator") Optional<String> creator,
                                @RequestParam(name="date") Optional<List<ZonedDateTime>> dates,
                                @RequestParam(name="competition") Optional<List<String>> competitions,
                                @RequestParam(name="category") Optional<List<String>> categories,
                                @RequestParam(name="magazine") Optional<List<String>> magazines,
                                @RequestParam(name="order") Optional<String> order
 */
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
            List<String> ids = new ArrayList<>();

            for (ResourceNode r : graphQueryResponse) {
                ids.add(r.getResourceID());
            }


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

        //TODO: altern the calls order to the databases: first to the GraphDB and, inside the loop, the call to the VectorDB
        List<ResourceVectorDatabase> vectorQueryResponse = resourceVectorDatabaseService.getAllResources(page, size);
        for ( ResourceVectorDatabase r : vectorQueryResponse) {
            Resource resource = ResourceVectorDatabaseToResource.toResource(r);
//                    = new Resource();
//            resource.setID(r.getID());
//            resource.setTimestamp(ZonedDateTime.now());
//            resource.setTitle(r.getTitle());
//            resource.setLocalImage(r.getImage());

            //TODO: add the data from the node to the final Resource
            try {
                ResourceNode graphQueryResponse = resourceNodeService.getResourceNodeById(r.getID());
                ResourceNodeToResource.completeResource(resource, graphQueryResponse);
//                List<Property> properties = new ArrayList<>();
//                Competition competition = new Competition();
//                competition.setName(graphQueryResponse.getCompetition());
//                properties.add(competition);
//                resource.setProperties(properties);
//                System.out.println(" ==================== Properties: "+properties);
            } catch (Exception e) {
//                Log("Error getting ResourceNode {id:"+ r.getID() +"}...");
            }

            resources.add(resource);
        }

        return resources;
    }

    public Resource getResourceByImageSimilarity(String image) {
        return blank();
    }

    public Resource insertResource(Resource newResource){

        String existingId = resourceVectorDatabaseService.isImageAlredyInDatabase(newResource.getImage());
        if(existingId != null){
            throw new ImageAlredyExistsException("Image already in database", existingId);
        }

        //INSERT INTO GRAPH DB
        ResourceNode newResourceGraph = new ResourceNode(newResource);
        ResourceNode queryResultG = resourceNodeService.addResourceNode(newResourceGraph);

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
}
