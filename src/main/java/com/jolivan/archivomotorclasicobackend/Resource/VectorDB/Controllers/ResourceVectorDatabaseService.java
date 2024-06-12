package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceUpdateDTO;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions.ResourceVectorDatabaseNotUpdatedException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabase;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions.IdIsNullException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions.ImageAlredyExistsException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabaseImageSearchListsResponse;
import com.jolivan.archivomotorclasicobackend.Utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jolivan.archivomotorclasicobackend.Utils.LoggingUtils.Log;

@Service
public class ResourceVectorDatabaseService {
    ResourceVectorDatabaseRepository dbRepository;
    @Autowired
    public ResourceVectorDatabaseService(ResourceVectorDatabaseRepository ResourceVectorDatabaseRepositoryWeaviateAdapter) {
        this.dbRepository = ResourceVectorDatabaseRepositoryWeaviateAdapter;
    }

    public ResourceVectorDatabase getResource(String id){

        if(id == null) throw new IdIsNullException("Id is null");

        ResourceVectorDatabase resource = new ResourceVectorDatabase();
        Map<String, Object> queryResult;

        try {
            queryResult = dbRepository.getResourceById(id);
        } catch (Throwable e) {
            return resource;
        }

        //ERROR HANDLING
        if (queryResult.get("hasErrors") instanceof Boolean && (Boolean) queryResult.get("hasErrors")) {
            Log("Error getting resource {id:"+ id +"}...");
            Log((String)queryResult.get("errorNumber"));
            for(Pair<String, Throwable> p : (List<Pair<String, Throwable>>)queryResult.get("errorMessages")){
                Log("   -"+p.first);
            }
            return null;
        }


        //SETTING DATA
        resource.setID(id);

        if(queryResult.get("title") != null) {
            String textResult = queryResult.get("title").toString();
            resource.setTitle(textResult);
        } else {
            resource.setTitle("noTitle");
        }

        if(queryResult.get("description") != null) {
            String textResult = queryResult.get("description").toString();
            resource.setDescription(textResult);
        } else{
            resource.setDescription("noDescription");
        }


        String imageResult = queryResult.get("image").toString();
        resource.setImage(imageResult);

        return resource;
    }

    public List<ResourceVectorDatabase> getAllResources(int page, int size){
        List<ResourceVectorDatabase> resources = new ArrayList<ResourceVectorDatabase>();
        List<Map<String, Object>> queryResult;

        try {
            queryResult = dbRepository.getAllResources(page, size);
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            return null;
        }

        //TODO: error handling

        //EXTRACTING DATA FROM RESULT
        for (Map<String, Object> r : queryResult) {
            ResourceVectorDatabase resource = new ResourceVectorDatabase();
            resource.setID((String) r.get("id"));
            resource.setTitle((String) r.get("title"));
            resource.setDescription((String) r.get("description"));
            resource.setImage((String) r.get("image"));

            resources.add(resource);
        }

        return resources;
    }

    public List<ResourceVectorDatabase> searchResources(String title, String description) {
        List<Map<String, Object>> textSearchQueryResult;

        try {
            textSearchQueryResult = dbRepository.searchResources(title, description);
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            return null;
        }



        List<ResourceVectorDatabase> resources = new ArrayList<ResourceVectorDatabase>();
        System.out.println("In search by text similarity, distances are:");
        for (Map<String, Object> textResult : textSearchQueryResult) {
            System.out.println("   id: "+textResult.get("resourceId")+" - "+textResult.get("distance"));
            ResourceVectorDatabase resource = new ResourceVectorDatabase();
            resource.setID((String) textResult.get("resourceId"));
            resource.setTitle((String) textResult.get("title"));
            resource.setDescription((String) textResult.get("description"));

            Map<String, Object> resourceQueryResult;
            try {
                resourceQueryResult = dbRepository.getResourceById(resource.getID());
            } catch (Throwable e) {
                Log("!! Error: "+e.getMessage());
                return null;
            }

            resource.setImage((String) resourceQueryResult.get("image"));
            resources.add(resource);
        }

        return resources;
    }
    public List<String> getResourcesIds(){
        return dbRepository.getAllResourcesIds();
    }

    public List<ResourceVectorDatabase> searchResourcesWithPrefilter(String title, String description, List<String> ids) {
        return null;
    }


    public ResourceVectorDatabase addResource(ResourceVectorDatabase newResourceVector) throws ImageAlredyExistsException {
        Map<String, Object> imageData = new HashMap<>();
        imageData.put("title", newResourceVector.getTitle());
        imageData.put("description", newResourceVector.getDescription());
        imageData.put("image", newResourceVector.getImage());

        Map<String, Object> imageInsertResult = dbRepository.insertResource(imageData);

        Map<String, Object> textData = new HashMap<>();
        textData.put("title", newResourceVector.getTitle());
        textData.put("description", newResourceVector.getDescription());
        textData.put("id", imageInsertResult.get("id"));

        Boolean textInserted = dbRepository.insertText(textData);
        if(!textInserted) System.out.println("!! Error inserting text");

        ResourceVectorDatabase resource = new ResourceVectorDatabase();
        resource.setID((String) imageInsertResult.get("id"));
        resource.setTitle((String)((HashMap) imageInsertResult.get("properties")).get("title"));
        resource.setDescription((String)((HashMap) imageInsertResult.get("properties")).get("description"));
        resource.setImage((String)((HashMap) imageInsertResult.get("properties")).get("image"));

        return resource;
    }


    /**
     * Check if image is already in database
     * @param image in base64 format
     * @return id of the resource if image is already in database, null otherwise
     */

    public String isImageAlredyInDatabase(String image) {
        try{
            List<Map<String, Object>> queryResult = dbRepository.getResourcesByImageSimilarity(image, 1);
            for(Map<String, Object> r : queryResult){
                //if distance is less than 3.5762787E-5 (because if image is the same, distance == 3.5762787E-7)
                if(Double.compare((double)r.get("distance"), 3.5762787E-5) == -1 ) {
                    System.out.println("Image already in database");
                    return (String) r.get("id");
                }
            }
        } catch (Throwable e) {
            Log("Error in --> ResourceVectorDatabaseService.isImageAlredyInDatabase: "+e.getMessage());
        }

        return null;
    }

    public Boolean deleteResource(String requestId) {
        try {
            dbRepository.deleteResource(requestId);
        } catch (Throwable e) {
            return false;
        }
        try{
            dbRepository.deleteText(requestId);
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    public ResourceVectorDatabase updateResource(String id, ResourceUpdateDTO resourceUpdateDTO) {
        Map<String, Object> queryResult = dbRepository.getResourceById(id);

        //TODO: check if it's really necessary to update the database
        queryResult.put("title", resourceUpdateDTO.getTitle());
        queryResult.put("description", resourceUpdateDTO.getDescription());

        Boolean updatedOK = dbRepository.updateResource(id, queryResult);
        if(!updatedOK) throw new ResourceVectorDatabaseNotUpdatedException("Resource (image) not updated");
        updatedOK = dbRepository.updateText(id, queryResult);
        if(!updatedOK) throw new ResourceVectorDatabaseNotUpdatedException("Resource (text) not updated");

        ResourceVectorDatabase resource = new ResourceVectorDatabase();
        resource.setID(id);
        resource.setTitle(resourceUpdateDTO.getTitle());
        resource.setDescription(resourceUpdateDTO.getDescription());
        resource.setImage((String)queryResult.get("image"));
        return resource;
    }

    public ResourceVectorDatabaseImageSearchListsResponse searchResourcesByImage(String image, int limit) {
        List<ResourceVectorDatabase> similar = new ArrayList<>(), lessSimilar = new ArrayList<>();
        try{
            List<Map<String, Object>> queryResult = dbRepository.getResourcesByImageSimilarity(image, limit);
            for(Map<String, Object> r : queryResult){ //TODO: make it so > than mean is similar, < than mean is lessSimilar
                System.out.println("   id: "+r.get("id")+" - "+r.get("distance"));
                if(Double.compare((double)r.get("distance"), 3.5762787E-5) > 0 ) {
                    ResourceVectorDatabase resource = new ResourceVectorDatabase();
                    resource.setID((String) r.get("id"));
                    resource.setTitle((String) r.get("title"));
                    resource.setDescription((String) r.get("description"));
                    resource.setImage((String) r.get("image"));

                    if(Double.compare((double)r.get("distance"), 0.5) < 0 ) similar.add(resource); //distance is less than 0.5
                    else lessSimilar.add(resource);
                }
            }
        } catch (Throwable e) {
            Log("Error in --> ResourceVectorDatabaseService.isImageAlredyInDatabase: "+e.getMessage());
        }
        return new ResourceVectorDatabaseImageSearchListsResponse(similar, lessSimilar);
    }
}
