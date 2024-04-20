package com.jolivan.archivomotorclasicobackend.Resource.Controllers;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.*;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Exceptions.ResourceNodeNotFound;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.ResourceFormToResourceConverter;
import com.jolivan.archivomotorclasicobackend.Resource.RUtils.Base64FileConversor;
import com.jolivan.archivomotorclasicobackend.Security.SUtils.Session;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;


import static com.jolivan.archivomotorclasicobackend.Utils.LoggingUtils.*;

@RestController
public class ResourceController {
 
    private static final String URL = "http://localhost:3000";
    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @CrossOrigin(origins = URL)
    @GetMapping(value="/resources/ids")
    public List<String> getResourcesIds(){
        return resourceRepository.getResourcesIds();
    }

    @CrossOrigin(origins = URL)
    @GetMapping(value="/resources/{requestId:[0-9a-zA-Z-]+}")
    public Resource getResource(@PathVariable String requestId){
        Resource resource = null;

        try {
            resource = resourceRepository.getResource(requestId);
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            return resourceRepository.blank();
        }

        return resource;
    }

    @CrossOrigin(origins = URL)
    @GetMapping("/resources")
    List<Resource> getResources(@RequestParam(name = "page") Optional<String> page,
                                @RequestParam(name = "size") Optional<String> size,
                                @RequestParam(name="title") Optional<String> title,
                                @RequestParam(name="description") Optional<String> description,
                                @RequestParam(name="place") Optional<String> place,
                                @RequestParam(name="date") Optional<List<ZonedDateTime>> dates,
                                @RequestParam(name="competition") Optional<List<String>> competitions,
                                @RequestParam(name="category") Optional<List<String>> categories,
                                @RequestParam(name="magazine") Optional<List<String>> magazines,
                                @RequestParam(name="order") Optional<String> order,
                                @RequestParam(name="noimage") Optional<Boolean> noimage
                                ) {
        List<Resource> resources = null;

        try {
            resources = resourceRepository.getResources(page, size, title, description, place, dates, competitions, categories, magazines, order);
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            return new ArrayList<>();
        }

        if(noimage.isPresent() && noimage.get()){
            for(Resource r : resources){
                if(r.getImage() != null)r.setImage("noImage");
            }
        }


        return resources;
    }


    @CrossOrigin(origins = URL)
    @PostMapping(value = "/resources"/*, produces = MediaType.APPLICATION_JSON_VALUE*/, consumes = {"application/json"})
    public ResponseEntity<Resource> postResource(@RequestBody ResourceRequestDTO resourceRequestDTO) {
        Resource newResource = ResourceFormToResourceConverter.toResource(resourceRequestDTO);

        //TODO: delete this, will automatically detect user by auth
        //Check that nobody is trying to post a resource with a different creator
        if(!resourceRequestDTO.getCreator().equals(Session.getCurrentUserName())){
            return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.FORBIDDEN);
        }

        Resource result = null;
        try {
            result = resourceRepository.insertResource(newResource);
        } catch (UserNodeNotFound e) {
            return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.NOT_FOUND);
        }

        if(result == null) return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = URL)
    @PutMapping(value = "/resources/{requestId:[0-9a-zA-Z-]+}"/*, produces = MediaType.APPLICATION_JSON_VALUE*/, consumes = {"application/json"})
    public ResponseEntity<Resource> updateResource (@RequestBody ResourceRequestDTO resourceRequestDTO, @PathVariable String requestId) {
//        Resource updatingResource = ResourceFormToResourceConverter.toResource(resourceRequestDTO);
//
//        Resource result = resourceRepository.updateResource(updatingResource);
//
//        if(result == null) return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.INTERNAL_SERVER_ERROR);
//        return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.NOT_IMPLEMENTED);
    }


    @CrossOrigin(origins = URL)
    @DeleteMapping(value = "/resources/{requestId:[0-9a-zA-Z-]+}")
    public ResponseEntity<Boolean> deleteResource(@PathVariable String requestId) {
        Boolean deletedOK = false;
        try {
            deletedOK = resourceRepository.deleteResource(requestId);
        } catch (ResourceNodeNotFound e) {
            return new ResponseEntity<>(deletedOK, HttpStatus.NOT_FOUND);
        }

        if(!deletedOK) return new ResponseEntity<>(deletedOK, HttpStatus.INTERNAL_SERVER_ERROR);
        else return new ResponseEntity<>(deletedOK, HttpStatus.OK);
    }















    //!========================================== TESTS ==========================================
//    @CrossOrigin(origins = URL)
//    @PostMapping(value = "/resources/test/post"/*, produces = MediaType.APPLICATION_JSON_VALUE*/, consumes = {"application/json"})
//    public ResponseEntity<Resource> postResourceTest(@RequestBody ImageForm imageForm) {
//        System.out.println("------------------ POSTING RESOURCE ------------------");
//        System.out.println(imageForm.getImage());
//        System.out.println(imageForm.getTitle());
//        System.out.println(imageForm.getDescription());
//        System.out.println(imageForm.getCreator());
//        System.out.println("------------------------------------------------------");
//        return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.OK);
//    }

    @GetMapping("/resources/test/noimage")
    List<Resource> getResourcesNoImageDevelop(@RequestParam(name = "page") Optional<String> page, @RequestParam(name = "size") Optional<String> size) {
        List<Resource> resources = null;

        try {
            resources = resourceRepository.getResources(page, size, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            return new ArrayList<>();
        }

        for(Resource r : resources){
            r.setImage("noImage");
        }

        return resources;
    }

    @GetMapping(value="/resources/test/distance")
    public ResponseEntity<Resource> testResource() {

        try {
            resourceRepository.getResourceByImageSimilarity(
                    Base64FileConversor.encodeFileToBase64Binary(
                            "C:\\Users\\juane\\Documents\\TFG\\imgs\\cars-data\\primeras-descargadas\\1966-10-19-AM-#76-TC-JUAN-PICHON-LULUAGA-000.JPG"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Resource newResource = new Resource();
        return new ResponseEntity<>(newResource, HttpStatus.OK);
    }

}
