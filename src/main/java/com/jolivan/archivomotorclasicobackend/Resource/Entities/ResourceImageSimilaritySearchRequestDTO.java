package com.jolivan.archivomotorclasicobackend.Resource.Entities;

import lombok.Data;

@Data
public class ResourceImageSimilaritySearchRequestDTO {
    String image;
    int limit;
}
