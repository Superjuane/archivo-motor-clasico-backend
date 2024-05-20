package com.jolivan.archivomotorclasicobackend.Collections.Entities;

import lombok.Data;

import java.util.List;

@Data
public class CollectionCreateDTO {
    String title;
    List<String> resourcesIds;
}
