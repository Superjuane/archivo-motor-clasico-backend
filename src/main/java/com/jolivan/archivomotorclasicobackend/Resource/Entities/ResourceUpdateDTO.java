package com.jolivan.archivomotorclasicobackend.Resource.Entities;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Collection;

@Data
public class ResourceUpdateDTO {
    String title;
    String description;
    ZonedDateTime date;
    String competition;
    Collection<String> tags;
}
