package com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions;

public class ResourceForbidden extends RuntimeException {
    public ResourceForbidden(String mess) {
        super(mess);
    }
}
