package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabase;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.IdIsNullException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.ImageAlredyExistsException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.ResourceVectorDatabaseToResource;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.WeaviateResultConverter;
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

        String textResult = queryResult.get("title").toString();
        resource.setTitle(textResult);

        String descriptionResult = queryResult.get("description").toString();
        resource.setDescription(descriptionResult);

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

    public List<String> getResourcesIds(){
        return dbRepository.getAllResourcesIds();
    }

    public List<ResourceVectorDatabase> searchResourcesWithPrefilter(String title, String description, List<String> ids) {
        return null;
    }


    public ResourceVectorDatabase addResource(ResourceVectorDatabase newResourceVector) throws ImageAlredyExistsException {
        Map<String, Object> data = new HashMap<>();
        data.put("title", newResourceVector.getTitle());
        data.put("description", newResourceVector.getDescription());
        data.put("image", newResourceVector.getImage());

        Map<String, Object> queryResult = dbRepository.insertResource(data);

        ResourceVectorDatabase resource = new ResourceVectorDatabase();
        resource.setID((String) queryResult.get("id"));
        resource.setTitle((String)((HashMap) queryResult.get("properties")).get("title"));
        resource.setDescription((String)((HashMap) queryResult.get("properties")).get("description"));
        resource.setImage((String)((HashMap) queryResult.get("properties")).get("image"));

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

}
