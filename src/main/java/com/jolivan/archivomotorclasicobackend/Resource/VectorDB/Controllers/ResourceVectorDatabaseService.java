package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabase;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.ImageAlredyExistsException;
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

        ResourceVectorDatabase resource = new ResourceVectorDatabase();
        Map<String, Object> queryResult;

        try {
            queryResult = dbRepository.getResourceById(id);
        } catch (Throwable e) {
            throw new RuntimeException(e);
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


        //GETTING DATA
        String textResult = queryResult.get("text").toString();
        resource.setTitle(textResult);

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
            resource.setTitle((String) r.get("text"));
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
        data.put("text", newResourceVector.getTitle());
        data.put("image", newResourceVector.getImage());

        if(isImageAlredyInDatabase(newResourceVector.getImage())) {
            throw new ImageAlredyExistsException("Image already in database");
        }

        Map<String, Object> queryResult;
//        try {
            queryResult = dbRepository.insertResource(data);
//        } catch (Throwable e) {
//            Log("!! Error: "+e.getMessage());
//            return null;
//        }

        ResourceVectorDatabase resource = new ResourceVectorDatabase();
        resource.setID((String) queryResult.get("id"));
        resource.setTitle((String) queryResult.get("text"));
        resource.setImage((String) queryResult.get("image"));

        return resource;
    }

    public boolean isImageAlredyInDatabase(String image) {
        try{
            List<Map<String, Object>> queryResult = dbRepository.getResourcesByImageSimilarity(image, 1);
            for(Map<String, Object> r : queryResult){
                //if distance is less than 3.5762787E-5 (because if image is the same, distance == 3.5762787E-7)
                if(Double.compare((double)r.get("distance"), 3.5762787E-5) == -1 ) {
                    System.out.println("Image already in database");
                    return true;
                }
            }
        } catch (Throwable e) {
            Log("Error in --> ResourceVectorDatabaseService.isImageAlredyInDatabase: "+e.getMessage());
        }

        return false;
    }

}
