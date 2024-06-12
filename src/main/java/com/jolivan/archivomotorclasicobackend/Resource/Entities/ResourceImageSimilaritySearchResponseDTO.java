package com.jolivan.archivomotorclasicobackend.Resource.Entities;

import lombok.Data;

import java.util.List;

@Data
public class ResourceImageSimilaritySearchResponseDTO {
    List<Resource> similar;
    List<Resource> lessSimilar;

    public ResourceImageSimilaritySearchResponseDTO(List<Resource> similar, List<Resource> lessSimilar) {
        this.similar = similar;
        this.lessSimilar = lessSimilar;
    }
}
