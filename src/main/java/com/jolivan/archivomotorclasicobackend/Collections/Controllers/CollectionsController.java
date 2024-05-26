package com.jolivan.archivomotorclasicobackend.Collections.Controllers;

import com.jolivan.archivomotorclasicobackend.Collections.Controllers.ExceptionControl.Exceptions.CollectionDoesntContainResourceException;
import com.jolivan.archivomotorclasicobackend.Collections.Controllers.ExceptionControl.Exceptions.CollectionNodeNotFoundException;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.Collection;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionCreateDTO;
import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionUpdateDTO;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ExceptionControl.ResourceNodeNotFoundException;
import com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions.UserForbiddenException;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CollectionsController {

    private final String URL = "http://localhost:3000";

    private final CollectionService collectionService;

    public CollectionsController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping("/collections/{id}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Collection> getCols(@PathVariable String id){
        if(id == null)
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.BAD_REQUEST);
        Collection result;
        try {
            result = collectionService.getCollectionById(id);
        } catch (CollectionNodeNotFoundException e){
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.NOT_FOUND);
        }

        if(result == null)
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/collections")
    @CrossOrigin(origins = URL)
    public ResponseEntity<List<Collection>> getAllCollectionsByUser(@RequestParam(name = "user", required = true) String username){
        List<Collection> result;
        try {
            result = collectionService.getAllCollectionsByUser(username);
        } catch (UserNodeNotFoundException e){
            return new ResponseEntity<>(collectionService.blankList(), HttpStatus.NOT_FOUND);
        }

        if(result == null)
            return new ResponseEntity<>(collectionService.blankList(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/collections")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Collection> createCollection(@RequestBody CollectionCreateDTO collectionDTO){
        //TODO:fix 4 return statements!!
        if(collectionDTO.getTitle() == null ){
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.BAD_REQUEST);
        }
        Collection result;
        try {
            result = collectionService.createCollection(collectionDTO);
        } catch (UserNodeNotFoundException | ResourceNodeNotFoundException e){
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.NOT_FOUND);
        }

        if(result == null)
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/collections/{collectionId}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> updateCollection(@PathVariable String collectionId, @RequestBody CollectionUpdateDTO collectionDTO){
        Collection result;
        if(collectionId == null)
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.BAD_REQUEST);

        try {
            result = collectionService.updateTitle(collectionId, collectionDTO.getNewTitle());
        } catch (UserNodeNotFoundException | CollectionNodeNotFoundException e ){
            Map<String, String> body = new HashMap<>();
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (UserForbiddenException e) {
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.FORBIDDEN);
        }

        if(result == null)
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/collections/{collectionId}/resources/{resourceId}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> updateCollection(@PathVariable String collectionId, @PathVariable String resourceId){
        Collection result;
        if(collectionId == null)
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.BAD_REQUEST);

        try {
            result = collectionService.addResource(collectionId, resourceId);
        } catch (UserNodeNotFoundException | CollectionNodeNotFoundException |ResourceNodeNotFoundException e ){
            Map<String, String> body = new HashMap<>();
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (UserForbiddenException e) {
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.FORBIDDEN);
        }

        if(result == null)
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/collections/{collectionId}/resources/{resourceId}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> deleteResourceFromCollection(@PathVariable String collectionId, @PathVariable String resourceId){
        Collection result;
        Map<String, String> body = new HashMap<>();

        if(collectionId == null || resourceId == null) {
            body.put("message", "Collection ID or Resource ID can't be null");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        try {
            result = collectionService.deleteResourceFromCollection(collectionId, resourceId);
        } catch (UserNodeNotFoundException
                 | CollectionNodeNotFoundException
                 | ResourceNodeNotFoundException
                 | CollectionDoesntContainResourceException e ){
            body.put("message", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (UserForbiddenException e) { //TODO: MAKE HANDLER FOR THIS
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.FORBIDDEN);
        }

        if(result == null)
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/collections/{collectionId}")
    @CrossOrigin(origins = URL)
    public ResponseEntity<Object> deleteCollection(@PathVariable String collectionId){
        Boolean result;
        Map<String, String> body = new HashMap<>();

        if(collectionId == null) {
            body.put("message", "Collection ID can't be null");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        try {
            result = collectionService.deleteCollection(collectionId);
        } catch (UserNodeNotFoundException e ){
            body.put("message", "User not found");
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (CollectionNodeNotFoundException e){
            body.put("message", "Collection not found");
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } catch (UserForbiddenException e) { //TODO: MAKE HANDLER FOR THIS
            return new ResponseEntity<>(collectionService.blank(), HttpStatus.FORBIDDEN);
        }

        if(result == null || !result)
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(body, HttpStatus.NO_CONTENT);
    }
}
