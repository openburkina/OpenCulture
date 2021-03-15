package com.openculture.org.service.dto;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.openculture.org.domain.Oeuvre} entity.
 */
public class OeuvreDTO implements Serializable {
    
    private Long id;

    private String titre;

    private Instant dateSortie;


    private Long typeOeuvreId;

    private Long regroupementId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Instant getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(Instant dateSortie) {
        this.dateSortie = dateSortie;
    }

    public Long getTypeOeuvreId() {
        return typeOeuvreId;
    }

    public void setTypeOeuvreId(Long typeOeuvreId) {
        this.typeOeuvreId = typeOeuvreId;
    }

    public Long getRegroupementId() {
        return regroupementId;
    }

    public void setRegroupementId(Long regroupementId) {
        this.regroupementId = regroupementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OeuvreDTO)) {
            return false;
        }

        return id != null && id.equals(((OeuvreDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OeuvreDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", dateSortie='" + getDateSortie() + "'" +
            ", typeOeuvreId=" + getTypeOeuvreId() +
            ", regroupementId=" + getRegroupementId() +
            "}";
    }
}
