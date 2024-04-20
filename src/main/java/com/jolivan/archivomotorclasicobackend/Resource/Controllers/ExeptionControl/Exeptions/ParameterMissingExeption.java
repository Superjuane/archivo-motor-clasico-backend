package com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions;

import java.util.List;

public class ParameterMissingExeption extends RuntimeException {
    private final List<String> parameters;

    public ParameterMissingExeption() {
        super();
        this.parameters = null;
    }

    public ParameterMissingExeption(String message) {
        super(message);
        this.parameters = null;
    }

    public ParameterMissingExeption(String message, List<String> parameters) {
        super(message);
        this.parameters = parameters;
    }
    public ParameterMissingExeption(List<String> parameters) {
        super();
        this.parameters = parameters;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
