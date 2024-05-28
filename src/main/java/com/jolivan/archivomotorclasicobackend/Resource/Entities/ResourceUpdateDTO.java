package com.jolivan.archivomotorclasicobackend.Resource.Entities;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class ResourceUpdateDTO {
    String title;
    String description;
    ZonedDateTime date;
    String competition;
    Map<String, String> magazineIssue;
    List<Map<String, String>> persons;
}
