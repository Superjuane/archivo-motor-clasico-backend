package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils;

import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ParameterMissingExeption;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Competition;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Date;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties.Property;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class ResourceFormToResourceConverter {

    public static Resource toResource(ResourceRequestDTO resourceRequestDTO) throws ParameterMissingExeption {
        return toResourcePrivate(resourceRequestDTO, false, false);
    }

    public static Resource toResource(ResourceRequestDTO resourceRequestDTO, Boolean completed) throws ParameterMissingExeption {
        return toResourcePrivate(resourceRequestDTO, completed, false);
    }

    public static Resource toResource(ResourceRequestDTO resourceRequestDTO, boolean completed, boolean withoutImage) {
        return toResourcePrivate(resourceRequestDTO, completed, withoutImage);
    }

    private static Resource toResourcePrivate(ResourceRequestDTO resourceRequestDTO, Boolean completed, Boolean withoutImage) throws ParameterMissingExeption {
        Resource newResource = new Resource();

        newResource.setId(resourceRequestDTO.getId());

        List<String> missingParameters = new ArrayList<String>();

        if (resourceRequestDTO.getTitle() != null) {
            newResource.setTitle(resourceRequestDTO.getTitle());
        } else {
            missingParameters.add("title");
        }


        if (resourceRequestDTO.getImage() != null || withoutImage) {
            if(withoutImage)
                newResource.setLocalImage("noImage");
            else
                newResource.setLocalImage(resourceRequestDTO.getImage());
        } else {
            missingParameters.add("image");
        }

        if(!missingParameters.isEmpty()){
            throw new ParameterMissingExeption(missingParameters);
        }
        if(resourceRequestDTO.getDescription() != null) {
            newResource.setDescription(resourceRequestDTO.getDescription());
        }

        //PROPERTIES
        List<Property> properties = new ArrayList<>();
        if(resourceRequestDTO.getDate() != null){
            properties.add(new Date(resourceRequestDTO.getDate()));
        }

        if(resourceRequestDTO.getCompetition() != null){
            properties.add(new Competition(resourceRequestDTO.getCompetition()));
        }

        newResource.setProperties(properties);


        //TODO: GO to GraphDB to look for properties such as MagazineIssue or Person.

        if(completed) newResource.setState("COMPLETED");
        else newResource.setState("PENDING");
        return newResource;
    }

}
