package com.openculture.org.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.openculture.org.domain.Artiste} entity.
 */
public class ArtisteDTO implements Serializable {
    
    private Long id;

    private String nom;

    private String prenom;


    private Long informationCivilId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Long getInformationCivilId() {
        return informationCivilId;
    }

    public void setInformationCivilId(Long informationCivilId) {
        this.informationCivilId = informationCivilId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtisteDTO)) {
            return false;
        }

        return id != null && id.equals(((ArtisteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArtisteDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", informationCivilId=" + getInformationCivilId() +
            "}";
    }
}
