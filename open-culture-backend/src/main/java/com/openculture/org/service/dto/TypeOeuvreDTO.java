package com.openculture.org.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.openculture.org.domain.TypeOeuvre} entity.
 */
public class TypeOeuvreDTO implements Serializable {
    
    private Long id;

    private String intitule;

    private Long nbOeuvre;
    
    public Long getId() {
        return id;
    }

    public Long getNbOeuvre() {
        return nbOeuvre;
    }

    public void setNbOeuvre(Long nbOeuvre) {
        this.nbOeuvre = nbOeuvre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOeuvreDTO)) {
            return false;
        }

        return id != null && id.equals(((TypeOeuvreDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOeuvreDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            "}";
    }
}
