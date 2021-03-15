package com.openculture.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A InformationCivil.
 */
@Entity
@Table(name = "information_civil")
public class InformationCivil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "numero_p")
    private String numeroP;

    @Column(name = "numero_s")
    private String numeroS;

    @OneToOne(mappedBy = "informationCivil")
    @JsonIgnore
    private Artiste artiste;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalite() {
        return nationalite;
    }

    public InformationCivil nationalite(String nationalite) {
        this.nationalite = nationalite;
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public InformationCivil dateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public InformationCivil lieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
        return this;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNumeroP() {
        return numeroP;
    }

    public InformationCivil numeroP(String numeroP) {
        this.numeroP = numeroP;
        return this;
    }

    public void setNumeroP(String numeroP) {
        this.numeroP = numeroP;
    }

    public String getNumeroS() {
        return numeroS;
    }

    public InformationCivil numeroS(String numeroS) {
        this.numeroS = numeroS;
        return this;
    }

    public void setNumeroS(String numeroS) {
        this.numeroS = numeroS;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public InformationCivil artiste(Artiste artiste) {
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
        if (!(o instanceof InformationCivil)) {
            return false;
        }
        return id != null && id.equals(((InformationCivil) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformationCivil{" +
            "id=" + getId() +
            ", nationalite='" + getNationalite() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", numeroP='" + getNumeroP() + "'" +
            ", numeroS='" + getNumeroS() + "'" +
            "}";
    }
}
