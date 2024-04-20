package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils;

import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ParameterMissingExeption;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.Resource;
import com.jolivan.archivomotorclasicobackend.Resource.Entities.ResourceRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class ResourceFormToResourceConverter {

    public static Resource toResource(ResourceRequestDTO resourceRequestDTO) throws ParameterMissingExeption {
        Resource newResource = new Resource();

        List<String> missingParameters = new ArrayList<String>();

        if (resourceRequestDTO.getTitle() != null) {
            newResource.setTitle(resourceRequestDTO.getTitle());
        } else {
            missingParameters.add("title");
        }

        if (resourceRequestDTO.getImage() != null) {
            newResource.setLocalImage(resourceRequestDTO.getImage());
        } else {
            missingParameters.add("image");
        }

        //TODO: delete this check, if authorized user = creator. If not, Error 401 Unauthorized
        if (resourceRequestDTO.getCreator() != null) {
            newResource.setCreator(resourceRequestDTO.getCreator());
        } else {
            missingParameters.add("creator");
        }

        if(missingParameters.size() > 0){
            throw new ParameterMissingExeption(missingParameters);
        }

        newResource.setState("PENDING");
        return newResource;
    }
}
