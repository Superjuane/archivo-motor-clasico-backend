package com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions;

public class ResourceForbiddenException extends RuntimeException {
    public ResourceForbiddenException(String mess) {
        super(mess);
        System.out.println("ResourceForbidden: " + mess);
    }
}
