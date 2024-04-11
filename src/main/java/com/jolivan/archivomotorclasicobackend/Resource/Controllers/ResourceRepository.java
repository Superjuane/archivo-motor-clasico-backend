package com.jolivan.archivomotorclasicobackend.Resource.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Property;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.*;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ResourceNodeService;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Utils.ResourceNodeToResource;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ResourceVectorDatabaseService;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabase;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.ResourceVectorDatabaseToResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jolivan.archivomotorclasicobackend.Utils.LoggingUtils.Log;
@Component
public class ResourceRepository {
    ResourceVectorDatabaseService resourceVectorDatabaseService;

    ResourceNodeService resourceNodeService;

    @Autowired
    public ResourceRepository(ResourceVectorDatabaseService resourceVectorDatabaseService, ResourceNodeService resourceNodeService){
        this.resourceVectorDatabaseService = resourceVectorDatabaseService;
        this.resourceNodeService = resourceNodeService;
    }

    public Resource getResource(String id) throws Throwable {

        //TODO: eliminate this developement shortcut
        if(id == null || id.isEmpty() || id.equals('4')) id = "f4f31cb6-3ed1-45c9-8931-b73f2298d9c5";

        //PREPARING RESOURCE
        Resource resource = new Resource();
        resource.setID(id);
        resource.setTimestamp(ZonedDateTime.now());
        resource.setImageUrl("https://i.pinimg.com/236x/b3/df/16/b3df164ad9902eae7ae657def1bd4c71.jpg");


        //VECTOR DATABASE DATA

        ResourceVectorDatabase Vresource = resourceVectorDatabaseService.getResource(id);
        resource.setTitle(Vresource.getTitle());
        resource.setLocalImage(Vresource.getImage());

        //GRAPH DATABASE DATA
        try {
            ResourceNode graphQueryResponse = resourceNodeService.getResourceNodeById(id);
            List<Property> properties = new ArrayList<Property>();
                Competition competition = new Competition();
                competition.setName(graphQueryResponse.getCompetition());
                properties.add(competition);
            resource.setProperties(properties);
        } catch (Exception e) {
//            Log("Error getting ResourceNode {id:"+ id +"}...");
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
                                       Optional<String> order){

        if(title.isPresent()
                || description.isPresent()
                || place.isPresent()
                || dates.isPresent()
                || competitions.isPresent()
                || categories.isPresent()
                || magazines.isPresent()
        ){
            return getSomeResources(title.isPresent()?title.get():null,
                    description.isPresent()?description.get():null,
                    place.isPresent()?place.get():null,
                    dates.isPresent()?dates.get():null,
                    competitions.isPresent()?competitions.get():null,
                    categories.isPresent()?categories.get():null,
                    magazines.isPresent()?magazines.get():null
                    );
        }


        //TODO: ordering!!!
        if (page.isPresent() && size.isPresent())
            return getResources(Integer.parseInt(page.get()), Integer.parseInt(size.get()));
        else
            return getResources(0, 20);
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
                ResourceVectorDatabase vectorQueryResponse = resourceVectorDatabaseService.getResource(r.getResourceID());
                ResourceVectorDatabaseToResource.completeResource(resource, vectorQueryResponse);
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
        resourceVectorDatabaseService.isImageAlredyInDatabase(image);
        return blank();
    }

    public Resource insertResource(Resource newResource){
        Resource result = new Resource();

        //INSERT INTO GRAPH DB //TODO: add more things to ImageForm apart from Title
        ResourceNode newResourceGraph = new ResourceNode();
        newResourceGraph.setTitle(newResource.getTitle());

        ResourceNode queryResultG = resourceNodeService.addResourceNode(newResourceGraph);

        //INSERT INTO VECTOR DB
        ResourceVectorDatabase newResourceVector = new ResourceVectorDatabase();
        newResourceVector.setTitle(newResource.getTitle());
        newResourceVector.setImage(newResource.getImage());

        ResourceVectorDatabase queryResultV;
//        try {
            queryResultV = resourceVectorDatabaseService.addResource(newResourceVector);
//        } catch (ImageAlredyExistsException e) {
//            Log("Error: "+e.getMessage());
//            return null;
//        }

        //UPDATE GRAPH DB with VectorDB new ID //TODO: add state pending
        queryResultG.setResourceID(queryResultV.getID());
        resourceNodeService.updateResourceNode(queryResultG);


        return result;
    }

    public List<String> getResourcesIds(){
        return resourceVectorDatabaseService.getResourcesIds();
    }
}
