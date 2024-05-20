package com.jolivan.archivomotorclasicobackend.Collections.ColUtils;

import com.jolivan.archivomotorclasicobackend.Collections.Entities.Collection;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;

import java.util.ArrayList;
import java.util.List;

public class CollectionNodeToCollection {

    private CollectionNodeToCollection() {
        throw new IllegalStateException("Utility class");
    }

    public static Collection toCollection(CollectionNode collectionNode){
        Collection collection = new Collection();
        collection.setId(String.valueOf(collectionNode.getId()));
        collection.setTitle(collectionNode.getTitle());

        List<String> resourcesIds = new ArrayList<>();
        for (ResourceNode resource : collectionNode.getResources()) {
           resourcesIds.add(resource.getResourceID());
        }
        collection.setResourcesIds(resourcesIds);

        collection.setCreator(collectionNode.getCreator().getName());

        return collection;
    }
}
