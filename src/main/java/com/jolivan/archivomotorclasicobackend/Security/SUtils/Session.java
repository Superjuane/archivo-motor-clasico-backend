package com.jolivan.archivomotorclasicobackend.Security.SUtils;

import org.neo4j.cypherdsl.core.Return;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Session {

    private Session() {
        throw new IllegalStateException("Utility class");
    }
    public static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }
}
