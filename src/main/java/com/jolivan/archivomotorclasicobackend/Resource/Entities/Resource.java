package com.jolivan.archivomotorclasicobackend.Resource.Entities;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Property;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@JsonComponent
public class Resource {
    String id;
    String title;
    String description;
    ImgType type;
    String image;
    List<Property> properties;
    String creator;
    String state;

    public void setImageUrl(String url) {
        this.image = url;
        this.type = ImgType.URL;
    }

    public void setLocalImage(String base64) {
        this.image = base64;
        this.type = ImgType.StringBase64;
    }
}
