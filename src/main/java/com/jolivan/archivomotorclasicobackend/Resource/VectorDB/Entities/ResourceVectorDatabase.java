package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Entities;

//@JsonComponent
public class ResourceVectorDatabase {
    private String ID;
    private String title = "NoTitle";
    private String description = "NoDescription";

    private String image;


    //Constructors, getters and setters
    public void setID(String number) {
        this.ID = number;
    }

    public String getID() {
        return this.ID;
    }

    public void setTitle(String sampleTitle) {
        this.title = sampleTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String sampleDescription) {
        this.description = sampleDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public void setImage(String sampleImage) {
        this.image = sampleImage;
    }

    public String getImage() {
        return this.image;
    }

}
