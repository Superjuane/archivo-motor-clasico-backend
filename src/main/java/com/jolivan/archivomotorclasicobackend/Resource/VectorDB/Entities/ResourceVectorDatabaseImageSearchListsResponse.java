package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities;

import lombok.Data;

import java.util.List;

@Data
public class ResourceVectorDatabaseImageSearchListsResponse {
    private List<ResourceVectorDatabase> similar;
    private List<ResourceVectorDatabase> lessSimilar;

    public ResourceVectorDatabaseImageSearchListsResponse(List<ResourceVectorDatabase> similar, List<ResourceVectorDatabase> lessSimilar) {
        this.similar = similar;
        this.lessSimilar = lessSimilar;
    }
}
