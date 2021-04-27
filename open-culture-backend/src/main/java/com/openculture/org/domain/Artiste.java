package com.openculture.org.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Artiste.
 */
@Entity
@Table(name = "artiste")
public class Artiste extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @OneToOne
    @JoinColumn(unique = true)
    private InformationCivil informationCivil;

    @OneToMany(mappedBy = "artiste")
    private Set<ArtisteOeuvre> artisteOeuvres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Artiste nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Artiste prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public InformationCivil getInformationCivil() {
        return informationCivil;
    }

    public Artiste informationCivil(InformationCivil informationCivil) {
        this.informationCivil = informationCivil;
        return this;
    }

    public void setInformationCivil(InformationCivil informationCivil) {
        this.informationCivil = informationCivil;
    }

    public Set<ArtisteOeuvre> getArtisteOeuvres() {
        return artisteOeuvres;
    }

    public Artiste artisteOeuvres(Set<ArtisteOeuvre> artisteOeuvres) {
        this.artisteOeuvres = artisteOeuvres;
        return this;
    }

    public Artiste addArtisteOeuvre(ArtisteOeuvre artisteOeuvre) {
        this.artisteOeuvres.add(artisteOeuvre);
        artisteOeuvre.setArtiste(this);
        return this;
    }

    public Artiste removeArtisteOeuvre(ArtisteOeuvre artisteOeuvre) {
        this.artisteOeuvres.remove(artisteOeuvre);
        artisteOeuvre.setArtiste(null);
        return this;
    }

    public void setArtisteOeuvres(Set<ArtisteOeuvre> artisteOeuvres) {
        this.artisteOeuvres = artisteOeuvres;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artiste)) {
            return false;
        }
        return id != null && id.equals(((Artiste) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Artiste{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            "}";
    }
}
