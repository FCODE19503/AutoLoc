package com.example.activite.exception;

/** Exception levée lorsqu'une ressource métier demandée est absente. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " introuvable avec l'identifiant " + id);
    }
}
