package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers;

import java.util.List;
import java.util.Map;

public interface ResourceVectorDatabaseRepository {
    /**
     * Returns a list of all resources ids
     * @return List of all ids
     */
    public List<String> getAllResourcesIds();

    /**
     * Returns a list of all resources
     * @return List of all resources
     */
    public List<Map<String, Object>> getAllResources();

    /**
     * Returns a list of all resources
     *
     * @param page Page number
     * @param size Size of the page
     * @return List of all resources
     */
    public List<Map<String, Object>> getAllResources(int page, int size);

    /**
     * Returns a resource by its id
     * @param id Resource id
     * @return Resource
     * @throws Throwable
     */
    public Map<String, Object> getResourceById(String id);

    public List<Map<String, Object>> getResourcesByImageSimilarity(String title, Integer limit) throws Throwable;

    List<Map<String, Object>> searchResources(String title, String description);

    /**
     * Inserts data into the database
     * @param data Data to insert
     */
    public Map<String, Object> insertResource(Map<String, Object> data);

    /**
     * Updates data in the database
     * @param id Id of the resource to update
     * @param data Data to update
     */
    public Boolean updateResource(String id, Map<String, Object> data);

    /**
     * Deletes a resource from the database
     *
     * @param id Id of the resource to delete
     */
    public Boolean deleteResource(String id);

    public Boolean insertText(Map<String, Object> textData);

    public Boolean updateText(String id, Map<String, Object> data);

    public Boolean deleteText(String id);
}
