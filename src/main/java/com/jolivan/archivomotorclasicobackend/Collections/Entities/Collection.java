package com.jolivan.archivomotorclasicobackend.Collections.Entities;

import jakarta.annotation.Resources;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;

@Data
@JsonComponent
public class Collection {
    String id;
    String title;
    String creator;
    List<String> resourcesIds;
}
