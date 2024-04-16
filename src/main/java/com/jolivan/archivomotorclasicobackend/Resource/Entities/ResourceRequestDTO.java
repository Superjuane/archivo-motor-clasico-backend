package com.jolivan.archivomotorclasicobackend.Resource.Entities;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Collection;

@Data
public class ResourceRequestDTO {
    String id;
    String title;
    String description;
    String image;
    ZonedDateTime date;
    String creator;
    String competition;
    Collection<String> tags;

}
