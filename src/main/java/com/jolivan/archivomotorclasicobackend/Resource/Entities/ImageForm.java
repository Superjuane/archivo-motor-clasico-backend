package com.jolivan.archivomotorclasicobackend.Resource.Entities;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Collection;

@Data
public class ImageForm {
    String id;
    String title;
    String description;
    String image;
    ZonedDateTime date;
    String creator;
    Collection<String> tags;

}
