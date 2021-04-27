package com.openculture.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ArtisteOeuvre.
 */
@Entity
@Table(name = "artiste_oeuvre")
public class ArtisteOeuvre extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JsonIgnoreProperties(value = "artisteOeuvres", allowSetters = true)
    private Oeuvre oeuvre;

    @ManyToOne
    @JsonIgnoreProperties(value = "artisteOeuvres", allowSetters = true)
    private Artiste artiste;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public ArtisteOeuvre role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public ArtisteOeuvre oeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
        return this;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public ArtisteOeuvre artiste(Artiste artiste) {
        this.artiste = artiste;
        return this;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtisteOeuvre)) {
            return false;
        }
        return id != null && id.equals(((ArtisteOeuvre) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArtisteOeuvre{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            "}";
    }
}
