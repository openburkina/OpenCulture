package com.openculture.org.service;

public class EntityUsedInAnotherException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityUsedInAnotherException() {
        super("Entity is used in other entity !");
    }

}
