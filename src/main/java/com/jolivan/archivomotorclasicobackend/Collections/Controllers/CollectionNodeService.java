package com.jolivan.archivomotorclasicobackend.Collections.Controllers;

import com.jolivan.archivomotorclasicobackend.Collections.CUtils.CollectionNodeToCollection;
import com.jolivan.archivomotorclasicobackend.Collections.Controllers.ExceptionControl.Exceptions.CollectionNodeNotFoundException;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionNode;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.Collection;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ResourceNodeService;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions.UserForbiddenException;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.UserNodeService;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CollectionNodeService {
    private final CollectionNodeRepository collectionNodeRepository;
    private final UserNodeService userNodeService;
    private final ResourceNodeService resourceNodeService;

    @Autowired
    public CollectionNodeService(CollectionNodeRepository collectionNodeRepository, UserNodeService userNodeService, ResourceNodeService resourceNodeService) {
        this.collectionNodeRepository = collectionNodeRepository;
        this.userNodeService = userNodeService;
        this.resourceNodeService = resourceNodeService;
    }

    public List<CollectionNode> getAllCollectionNodes(){
        return collectionNodeRepository.findAll();
    }

    public Collection createCollectionNode(CollectionNode collectionNode, String username, List<String> resourcesIds){
        UserNode user = userNodeService.getUserNodeByUsername(username);
        List<ResourceNode> resources = new LinkedList<>();
        for(String resourceId : resourcesIds){
            resources.add(resourceNodeService.getResourceNodeById(resourceId));
        }
        collectionNode.setCreator(user);
        collectionNode.setResources(resources);

        collectionNode = collectionNodeRepository.save(collectionNode);
        return CollectionNodeToCollection.toCollection(collectionNode);
    }

    public List<Collection> getAllCollectionsByUser(String username) {
        UserNode user = userNodeService.getUserNodeByUsername(username);
        if(user == null){
            throw new UserNodeNotFoundException();
        }
        List<CollectionNode> collectionNodes = collectionNodeRepository.findByUsername(username);

        List<Collection> collections = new LinkedList<>();
        for(CollectionNode collectionNode : collectionNodes){
            collections.add(CollectionNodeToCollection.toCollection(collectionNode));
        }
        return collections;
    }

    public Collection getCollectionById(String collectionId) {
        CollectionNode collectionNode = collectionNodeRepository.findById(Long.valueOf(collectionId)).orElse(null);
        if(collectionNode == null){
            throw new CollectionNodeNotFoundException();
        }
        return CollectionNodeToCollection.toCollection(collectionNode);
    }

    public Collection updateCollection(String collectionId, String resourceId) {
        CollectionNode collectionNode = collectionNodeRepository.findById(Long.valueOf(collectionId)).orElse(null);
        if(collectionNode == null){
            throw new CollectionNodeNotFoundException();
        }
        ResourceNode resource = resourceNodeService.getResourceNodeById(resourceId);
        collectionNode.getResources().add(resource);
        collectionNode = collectionNodeRepository.save(collectionNode);
        return CollectionNodeToCollection.toCollection(collectionNode);
    }

    public List<Collection> getAllCollections() {
        List<CollectionNode> collectionNodes = collectionNodeRepository.findAll();
        List<Collection> collections = new LinkedList<>();
        for(CollectionNode collectionNode : collectionNodes){
            collections.add(CollectionNodeToCollection.toCollection(collectionNode));
        }
        return collections;
    }

    public Boolean deleteCollection(String collectionId, String username) {
        CollectionNode collectionNode = collectionNodeRepository.findById(Long.valueOf(collectionId)).orElse(null);
        if(collectionNode == null){
            throw new CollectionNodeNotFoundException();
        }
        if(!collectionNode.getCreator().getName().equals(username)){
            throw new UserForbiddenException();
        }
        try {
            collectionNodeRepository.delete(collectionNode);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
