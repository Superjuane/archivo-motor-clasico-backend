package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities.ResourceVectorDatabase;

public class ResourceVectorDatabaseToResource {
    public static Resource toResource(ResourceVectorDatabase resourceVectorDatabase){
        Resource resource = new Resource();
        resource.setId(resourceVectorDatabase.getID());
        resource.setTitle(resourceVectorDatabase.getTitle());
        resource.setDescription(resourceVectorDatabase.getDescription());
        resource.setLocalImage(resourceVectorDatabase.getImage());
        return resource;
    }

    public static Resource completeResource(Resource resource, ResourceVectorDatabase resourceVectorDatabase){
        resource.setId(resourceVectorDatabase.getID());
        resource.setTitle(resourceVectorDatabase.getTitle());
        resource.setDescription(resourceVectorDatabase.getDescription());
        resource.setLocalImage(resourceVectorDatabase.getImage());
        return resource;
    }

}
