package com.jolivan.archivomotorclasicobackend.Collections.Controllers;

import com.jolivan.archivomotorclasicobackend.Collections.Entities.Collection;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionCreateDTO;
import com.jolivan.archivomotorclasicobackend.Security.SecUtils.Session;
import com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions.UserForbiddenException;
import com.jolivan.archivomotorclasicobackend.User.Controllers.UserRepository;
import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository, UserRepository userRepository) {
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
    }

    public Collection blank(){
        return collectionRepository.blank();
    }

    public Collection createCollection(CollectionCreateDTO collectionDTO) {
        MyUser user = userRepository.findByUsername(Session.getCurrentUserName());
        if(user == null){
            throw new UserNodeNotFoundException();
        }
        return collectionRepository.createCollection(collectionDTO, user.getUsername());
    }

    public List<Collection> blankList() {
        List<Collection> result = new ArrayList<>();
        result.add(blank());
        return result;
    }

    public List<Collection> getAllCollectionsByUser(String username) {
        return collectionRepository.getAllCollectionsByUser(username);
    }

    public Collection addResource(String collectionId, String resourceId) {
        Collection collection = collectionRepository.getCollectionById(collectionId);
        if(!collection.getCreator().equals(Session.getCurrentUserName())){
            throw new UserForbiddenException();
        }
        return collectionRepository.addResource(collectionId, resourceId);
    }

    public List<Collection> getAllCollections() {
        return collectionRepository.getAllCollections();
    }

    public Boolean deleteCollection(String collectionId) {
        MyUser user = userRepository.findByUsername(Session.getCurrentUserName());

        if(user == null){
            throw new UserNodeNotFoundException();
        }

        return collectionRepository.deleteCollection(collectionId, user.getUsername());
    }

    public Collection getCollectionById(String id) {
        return collectionRepository.getCollectionById(id);
    }

    public Collection deleteResourceFromCollection(String collectionId, String resourceId) {
        Collection collection = collectionRepository.getCollectionById(collectionId); //THROWS CollectionNodeNotFoundException
        if(!collection.getCreator().equals(Session.getCurrentUserName())){
            throw new UserForbiddenException();
        }
        return collectionRepository.deleteResourceFromCollection(collectionId, resourceId);
    }

    public Collection updateTitle(String collectionId, String newTitle) {
        Collection collection = collectionRepository.getCollectionById(collectionId); //THROWS CollectionNodeNotFoundException
        if(!collection.getCreator().equals(Session.getCurrentUserName())){
            throw new UserForbiddenException();
        }
        return collectionRepository.updateTitle(collectionId, newTitle);
    }
}
