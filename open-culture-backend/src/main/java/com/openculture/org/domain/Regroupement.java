package com.openculture.org.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Regroupement.
 */
@Entity
@Table(name = "regroupement")
public class Regroupement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "intitule")
    private String intitule;

    @OneToMany(mappedBy = "regroupement")
    private Set<Oeuvre> oeuvres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Regroupement type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntitule() {
        return intitule;
    }

    public Regroupement intitule(String intitule) {
        this.intitule = intitule;
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Set<Oeuvre> getOeuvres() {
        return oeuvres;
    }

    public Regroupement oeuvres(Set<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
        return this;
    }

    public Regroupement addOeuvre(Oeuvre oeuvre) {
        this.oeuvres.add(oeuvre);
        oeuvre.setRegroupement(this);
        return this;
    }

    public Regroupement removeOeuvre(Oeuvre oeuvre) {
        this.oeuvres.remove(oeuvre);
        oeuvre.setRegroupement(null);
        return this;
    }

    public void setOeuvres(Set<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regroupement)) {
            return false;
        }
        return id != null && id.equals(((Regroupement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Regroupement{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", intitule='" + getIntitule() + "'" +
            "}";
    }
}
