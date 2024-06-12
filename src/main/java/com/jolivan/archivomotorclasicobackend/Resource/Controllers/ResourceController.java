package com.jolivan.archivomotorclasicobackend.Resource.Controllers;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.*;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ExceptionControl.ResourceNodeNotFoundException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions.IdIsNullException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.ResourceFormToResourceConverter;
import com.jolivan.archivomotorclasicobackend.Security.SecUtils.Session;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;


import static com.jolivan.archivomotorclasicobackend.Utils.LoggingUtils.*;

@RestController
public class ResourceController {

//    private static final String URL_develop = ;
//    private static final String URL = ;
    
//    private static final String[] URLS = new String[] {"http://localhost:3000", "https://archivo-motor-clasico-frontend-git-master-superjuanes-projects.vercel.app"};
    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/ids")
    public List<String> getResourcesIds(){
        return resourceRepository.getResourcesIds();
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/properties/competitions")
    public List<String> getResourcesCompetitions(@RequestParam(name = "competition", required = false) String competition){
        return resourceRepository.getResourcesCompetitions(competition);
    }
    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/properties/magazines")
    public List<String> getResourcesMagazinesNames(@RequestParam(name = "magazine", required = false) String magazine){
        return resourceRepository.getResourcesMagazinesNames(magazine);
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/properties/magazineNumbers")
    public List<Integer> getResourcesMagazinesNumbers(@RequestParam(name = "magazine", required = false) String magazineName){
        return resourceRepository.getResourcesMagazinesNumbers(magazineName);
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/properties/magazineIssues")
    public List<String> getResourcesMagazineIssues(@RequestParam(name = "magazine") Optional<String> magazine){
        return resourceRepository.getResourcesMagazineIssues(magazine);
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/properties/persons")
    public List<String> getResourcesPersons(@RequestParam(name = "person", required = false) String person){
        return resourceRepository.getResourcesPersons(person);
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/{requestId:[0-9a-zA-Z-]+}")
    public Resource getResource(@PathVariable String requestId, @RequestParam(name="noimage", required = false) Boolean noimage){
        Resource resource = null;

        try {
            resource = resourceRepository.getResource(requestId);
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            return resourceRepository.blank();
        }
        if(noimage != null && noimage){
            if(resource.getImage() != null)resource.setImage("noImage");
        }

        return resource;
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping(value="/resources/user")
    public List<Resource> getUserResources(@RequestParam(name="user") String username, @RequestParam(name="noimage", required = false) Boolean noimage){
        List<Resource> resources = null;
        try {
            resources = resourceRepository.getUserResources(username);
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            resources.add(resourceRepository.blank());
        }

        if(noimage != null && noimage){
            for(Resource r : resources){
                if(r.getImage() != null)r.setImage("noImage");
            }
        }

        return resources;
    }

    @CrossOrigin/*(origins = URLS)*/
    @GetMapping("/resources")
    List<Resource> getResources(@RequestParam(name = "page", required = false) String page,
                                @RequestParam(name = "size", required = false) String size,
                                @RequestParam(name="title", required = false) String title,
                                @RequestParam(name="description", required = false) String description,
                                @RequestParam(name="date", required = false) List<ZonedDateTime> dates,
                                @RequestParam(name="competition", required = false) String competition,
                                @RequestParam(name="magazine", required = false) String magazine,
                                @RequestParam(name="number", required = false) Integer number,
                                @RequestParam(name="persons", required = false) List<String> persons,
                                @RequestParam(name="order", required = false) String order,
                                @RequestParam(name="noimage", required = false) Boolean noimage
                                ) {
        List<Resource> resources = null;

        try {
            resources = resourceRepository.getResources(page, size, title, description, dates, competition, magazine, number, persons, order);
        } catch (Throwable e) {
            Log("!! Error: "+e.getMessage());
            return new ArrayList<>();
        }

        if(noimage != null && noimage){
            for(Resource r : resources){
                if(r.getImage() != null)r.setImage("noImage");
            }
        }

        return resources;
    }


    @CrossOrigin/*(origins = URLS)*/
    @PostMapping(value = "/resources/imagesearch", consumes = {"application/json"})
    public ResponseEntity<Object> postImageSearch(@RequestBody ResourceImageSimilaritySearchRequestDTO imageSimilaritySearchDTO) {
        ResourceImageSimilaritySearchResponseDTO result = null;
        Map<String, Object> body = new HashMap<>();

        if(imageSimilaritySearchDTO.getLimit() < 1 ) imageSimilaritySearchDTO.setLimit(15);

        try {
            result = resourceRepository.getResourceByImageSimilarity(imageSimilaritySearchDTO.getImage(), imageSimilaritySearchDTO.getLimit());
        } catch (Exception e) {
            body.put("error", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @CrossOrigin/*(origins = URLS)*/
    @PostMapping(value = "/resources"/*, produces = MediaType.APPLICATION_JSON_VALUE*/, consumes = {"application/json"})
    public ResponseEntity<Resource> postResource(@RequestBody ResourceRequestDTO resourceRequestDTO) {
        Resource newResource = ResourceFormToResourceConverter.toResource(resourceRequestDTO);

        String creator = Session.getCurrentUserName();
        if(creator == null){
            return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.FORBIDDEN);
        }

        Resource result = null;
        try {
            result = resourceRepository.insertResource(creator, newResource, resourceRequestDTO);
        } catch (UserNodeNotFoundException e) {
            return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.NOT_FOUND);
        }

        if(result == null) return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin/*(origins = URLS)*/
    @PutMapping(value = "/resources/{requestId:[0-9a-zA-Z-]+}"/*, produces = MediaType.APPLICATION_JSON_VALUE*/, consumes = {"application/json"})
    public ResponseEntity<Resource> updateResource (@RequestBody ResourceUpdateDTO resourceUpdateDTO, @PathVariable String requestId) {
        Resource result = null;

        try {
            result = resourceRepository.updateResource(requestId, resourceUpdateDTO);
        } catch (IdIsNullException e) {
            return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.BAD_REQUEST);
        }

        if(result == null) return new ResponseEntity<>(resourceRepository.blank(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @CrossOrigin/*(origins = URLS)*/
    @DeleteMapping(value = "/resources/{requestId:[0-9a-zA-Z-]+}")
    public ResponseEntity<Boolean> deleteResource(@PathVariable String requestId) {
        Boolean deletedOK = false;
        try {
            deletedOK = resourceRepository.deleteResource(requestId);
        } catch (ResourceNodeNotFoundException e) {
            return new ResponseEntity<>(deletedOK, HttpStatus.NOT_FOUND);
        }

        if(!deletedOK) return new ResponseEntity<>(deletedOK, HttpStatus.INTERNAL_SERVER_ERROR);
        else return new ResponseEntity<>(deletedOK, HttpStatus.NO_CONTENT);
    }















    //!========================================== TESTS ==========================================

//    @GetMapping("/resources/test/noimage")
//    List<Resource> getResourcesNoImageDevelop(@RequestParam(name = "page") Optional<String> page, @RequestParam(name = "size") Optional<String> size) {
//        List<Resource> resources = null;
//
//        try {
//            resources = resourceRepository.getResources(page, size, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
//        } catch (Throwable e) {
//            Log("!! Error: "+e.getMessage());
//            return new ArrayList<>();
//        }
//
//        for(Resource r : resources){
//            r.setImage("noImage");
//        }
//
//        return resources;
//    }

//    @GetMapping(value="/resources/test/distance")
//    public ResponseEntity<Resource> testResource() {
//
//        try {
//            resourceRepository.getResourceByImageSimilarity(
//                    Base64FileConversor.encodeFileToBase64Binary(
//                            "C:\\Users\\juane\\Documents\\TFG\\imgs\\cars-data\\primeras-descargadas\\1966-10-19-AM-#76-TC-JUAN-PICHON-LULUAGA-000.JPG"), 3);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        Resource newResource = new Resource();
//        return new ResponseEntity<>(newResource, HttpStatus.OK);
//    }

}
