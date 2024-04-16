package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities;

import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import lombok.Data;
import lombok.NoArgsConstructor;

//@JsonComponent
@Data
@NoArgsConstructor
public class ResourceVectorDatabase {
    private String ID;
    private String title = "NoTitle";
    private String description = "NoDescription";
    private String image;

    public ResourceVectorDatabase(Resource resource){
        this.ID = resource.getID();
        this.title = resource.getTitle();
        this.description = resource.getDescription();
        this.image = resource.getImage();
    }

}
