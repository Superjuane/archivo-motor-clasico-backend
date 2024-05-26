package com.jolivan.archivomotorclasicobackend.Collections.Controllers;

import com.jolivan.archivomotorclasicobackend.Collections.Entities.Collection;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionCreateDTO;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CollectionRepository {

    private final CollectionNodeService collectionNodeService;

    @Autowired
    public CollectionRepository(CollectionNodeService collectionNodeService) {
        this.collectionNodeService = collectionNodeService;
    }

    public Collection blank(){
        return new Collection();
    }


    public Collection createCollection(CollectionCreateDTO collectionDTO, String username) {
        CollectionNode collection = new CollectionNode();
        collection.setTitle(collectionDTO.getTitle());
        return collectionNodeService.createCollectionNode(collection, username, collectionDTO.getResourcesIds());
    }

    public List<Collection> getAllCollectionsByUser(String username) {
        return collectionNodeService.getAllCollectionsByUser(username);
    }

    public Collection getCollectionById(String collectionId) {
        return collectionNodeService.getCollectionById(collectionId);
    }

    public Collection addResource(String collectionId, String resourceId) {
        return collectionNodeService.addResource(collectionId, resourceId);
    }

    public List<Collection> getAllCollections() {
        return collectionNodeService.getAllCollections();
    }

    public Boolean deleteCollection(String collectionId, String username) {
        return collectionNodeService.deleteCollection(collectionId, username);
    }

    public Collection deleteResourceFromCollection(String collectionId, String resourceId) {
        return collectionNodeService.deleteResourceFromCollection(collectionId, resourceId);
    }

    public Collection updateTitle(String collectionId, String newTitle) {
        return collectionNodeService.updateTitle(collectionId, newTitle);
    }
}
