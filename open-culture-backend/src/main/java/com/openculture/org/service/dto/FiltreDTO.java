package com.openculture.org.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.openculture.org.domain.Artiste} entity.
 */
public class FiltreDTO implements Serializable {

    private Long id;

    private String string;

    public Long getId() {
        return id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
