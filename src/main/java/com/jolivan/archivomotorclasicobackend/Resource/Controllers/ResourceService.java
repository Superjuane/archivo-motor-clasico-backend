package com.jolivan.archivomotorclasicobackend.Resource.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceImageSimilaritySearchResponseDTO;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceRequestDTO;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<String> getResourcesIds() {
        return resourceRepository.getResourcesIds();
    }

    public List<String> getResourcesCompetitions(String competition) {
        return resourceRepository.getResourcesCompetitions(competition);
    }

    public List<String> getResourcesMagazinesNames(String magazine) {
        return resourceRepository.getResourcesMagazinesNames(magazine);
    }

    public List<Integer> getResourcesMagazinesNumbers(String magazineName) {
        return resourceRepository.getResourcesMagazinesNumbers(magazineName);
    }

    public List<String> getResourcesMagazineIssues(Optional<String> magazine) {
        return resourceRepository.getResourcesMagazineIssues(magazine);
    }

    public List<String> getResourcesPersons(String person) {
        return resourceRepository.getResourcesPersons(person);
    }

    public Resource getResource(String requestId) {
        return resourceRepository.getResource(requestId);
    }

    public Resource blank() {
        return resourceRepository.blank();
    }

    public List<Resource> getUserResources(String username) {
        return resourceRepository.getUserResources(username);
    }

    public List<Resource> getResources(String page, String size, String title, String description, List<ZonedDateTime> dates, String competition, String magazine, Integer number, List<String> persons, String order) {
        return resourceRepository.getResources(page, size, title, description, dates, competition, magazine, number, persons, order);
    }

    public ResourceImageSimilaritySearchResponseDTO getResourceByImageSimilarity(String image, int limit) {
        return resourceRepository.getResourceByImageSimilarity(image, limit);
    }

    public Resource insertResource(String creator, Resource newResource, ResourceRequestDTO resourceRequestDTO) {
        return resourceRepository.insertResource(creator, newResource, resourceRequestDTO);
    }

    public Resource updateResource(String requestId, ResourceUpdateDTO resourceUpdateDTO) {
        return resourceRepository.updateResource(requestId, resourceUpdateDTO);
    }

    public Boolean deleteResource(String requestId) {
        return resourceRepository.deleteResource(requestId);
    }
}
