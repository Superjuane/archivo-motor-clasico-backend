package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceUpdateDTO;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ExceptionControl.ResourceNodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceNodeService {
    private final ResourceNodeRepository resourceNodeRepository;

    @Autowired
    public ResourceNodeService(ResourceNodeRepository ResourceNodeRepository) {
        this.resourceNodeRepository = ResourceNodeRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ResourceNode> getAllResourceNodes(){
        return resourceNodeRepository.findAll();
    }

    public List<ResourceNode> searchResources(String place, List<ZonedDateTime> dates, List<String> competitions, List<String> magazines) {
        ZonedDateTime firstDate = null, lastDate = null;
        if(dates != null && !dates.isEmpty()){
            firstDate = dates.get(0);
            lastDate = dates.get(0);
            if(dates.size() > 1) {
                for (int i = 1; i < dates.size(); i++) {
                    if (dates.get(i).isBefore(firstDate))
                        firstDate = dates.get(i);
                    else if (dates.get(i).isAfter(lastDate))
                        lastDate = dates.get(i);
                }
            }
        }
        //TODO: fix this mess. Using ZonedDateTime, Neo4j won't compare the dates, so we have to split them into year, month and day...
        //*     with this method, we are always comparing all nodes by date, because sending the firstYear as null doesn't work.
        Integer firstYear = firstDate != null? firstDate.getYear() : 0;
        Integer firstMonth = firstDate != null? firstDate.getMonth().getValue() : 1;
        Integer firstDay = firstDate != null? firstDate.getDayOfMonth() : 1;
        Integer lastYear = lastDate != null? lastDate.getYear() : 9999;
        Integer lastMonth = lastDate != null? lastDate.getMonth().getValue() : 12;
        Integer lastDay = lastDate != null? lastDate.getDayOfMonth() : 31;

        System.out.println("     ·Searching resources | firstDate: " + firstDate + " | lastDate: " + lastDate);
        List <ResourceNode> result = resourceNodeRepository.searchResources(place, firstYear, firstMonth, firstDay, lastYear, lastMonth, lastDay, competitions, magazines);
        return resourceNodeRepository.searchResources(place, firstYear, firstMonth, firstDay, lastYear, lastMonth, lastDay, competitions, magazines);
    }

    public ResourceNode getResourceNodeById(String id) throws ResourceNodeNotFoundException {
        ResourceNode r = resourceNodeRepository.findById(id);
        if(r == null){
            throw new ResourceNodeNotFoundException();
        }
        return r;
    }

    public ResourceNode addResourceNode(ResourceNode resourceNode){
        System.out.println("Adding resource node");
        return resourceNodeRepository.save(resourceNode);
    }

    public ResourceNode updateResourceNode(String id, ResourceUpdateDTO resourceUpdateDTO)  {
        ResourceNode resourceNodeFromDB =  resourceNodeRepository.findById(id);

        if(resourceNodeFromDB != null){
            if(resourceUpdateDTO.getTitle() != null) resourceNodeFromDB.setTitle(resourceUpdateDTO.getTitle());
            if(resourceUpdateDTO.getDate() != null) resourceNodeFromDB.setDate(resourceUpdateDTO.getDate());
            if(resourceUpdateDTO.getCompetition() != null) resourceNodeFromDB.setCompetition(resourceUpdateDTO.getCompetition());
            //? Esta hay que hacerla resourceNodeFromDBVal.setMagazineIssue(resourceNode.getMagazineIssue());
            //! esta no! resourceNodeFromDBVal.setCreator(resourceNode.getCreator());

            return resourceNodeRepository.save(resourceNodeFromDB);
        }else{
            throw new ResourceNodeNotFoundException();
        }
    }

    public Boolean deleteResourceNode(String id) {
        resourceNodeRepository.deleteById(id);
        return true;
    }

    public ResourceNode updateResourceNodeId(Long queryId, String newResourceId) {
        Optional<ResourceNode> resourceNodeFromDB =  resourceNodeRepository.findById(queryId);
        if(resourceNodeFromDB.isPresent()){
            ResourceNode resourceNodeFromDBVal = resourceNodeFromDB.get();
            resourceNodeFromDBVal.setResourceID(newResourceId);
            resourceNodeFromDBVal.setState("COMPLETED");
            resourceNodeRepository.save(resourceNodeFromDBVal);
        }else{
            return null;
        }
        return resourceNodeFromDB.get();
    }

    public void joinResourceToUser(String resourceId, String userId) {
        resourceNodeRepository.joinResourceToUser(resourceId, userId);
    }

    public void joinResourceToUser(Long id, String userId) {
        resourceNodeRepository.joinResourceToUser(id, userId);
    }

    public List<String> getResourcesCompetitions() {
        return resourceNodeRepository.getResourcesCompetitions();
    }

    public List<String> getResourcesMagazines() {
        return resourceNodeRepository.getResourcesMagazines();
    }

    public List<String> getResourcesMagazineIssues(String magazineName) {
        return resourceNodeRepository.getResourcesMagazineIssues(magazineName);
    }

    public List<ResourceNode> searchResourcesByUser(String username) {
        return resourceNodeRepository.findByUser(username);
    }
}
