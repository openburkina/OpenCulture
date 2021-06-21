package com.openculture.org.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.openculture.org.domain.Regroupement} entity.
 */
public class RegroupementDTO implements Serializable {

    private Long id;

    private TypeRegroupementDTO typeRegroupementDTO;

    private Long typeRegroupementId;

    private String intitule;

    public Long getId() {
        return id;
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

    public TypeRegroupementDTO getTypeRegroupementDTO() {
        return typeRegroupementDTO;
    }

    public void setTypeRegroupementDTO(TypeRegroupementDTO typeRegroupementDTO) {
        this.typeRegroupementDTO = typeRegroupementDTO;
    }

    public Long getTypeRegroupementId() {
        return typeRegroupementId;
    }

    public void setTypeRegroupementId(Long typeRegroupementId) {
        this.typeRegroupementId = typeRegroupementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegroupementDTO)) {
            return false;
        }

        return id != null && id.equals(((RegroupementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegroupementDTO{" +
            "id=" + getId() +
            ", type='" + getTypeRegroupementDTO() + "'" +
            ", intitule='" + getIntitule() + "'" +
            "}";
    }
}
