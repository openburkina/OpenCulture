package com.openculture.org.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.openculture.org.domain.ArtisteOeuvre} entity.
 */
public class ArtisteOeuvreDTO implements Serializable {
    
    private Long id;

    private String role;


    private Long oeuvreId;

    private Long artisteId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getOeuvreId() {
        return oeuvreId;
    }

    public void setOeuvreId(Long oeuvreId) {
        this.oeuvreId = oeuvreId;
    }

    public Long getArtisteId() {
        return artisteId;
    }

    public void setArtisteId(Long artisteId) {
        this.artisteId = artisteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtisteOeuvreDTO)) {
            return false;
        }

        return id != null && id.equals(((ArtisteOeuvreDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArtisteOeuvreDTO{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", oeuvreId=" + getOeuvreId() +
            ", artisteId=" + getArtisteId() +
            "}";
    }
}
