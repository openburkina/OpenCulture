package com.openculture.org.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TypeRegroupement.
 */
@Entity
@Table(name = "type_regroupement")
public class TypeRegroupement extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "intitule")
    private String intitule;

    @OneToMany(mappedBy = "typeRegroupement")
    private Set<Regroupement> regroupements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public TypeRegroupement intitule(String intitule) {
        this.intitule = intitule;
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }


    public Set<Regroupement> getRegroupements() {
        return regroupements;
    }

    public TypeRegroupement regroupements(Set<Regroupement> regroupements) {
        this.regroupements = regroupements;
        return this;
    }

    public TypeRegroupement addRegroupement(Regroupement regroupement) {
        this.regroupements.add(regroupement);
        regroupement.setTypeRegroupement(this);
        return this;
    }

    public TypeRegroupement removeRegroupement(Regroupement regroupement) {
        this.regroupements.remove(regroupement);
        regroupement.setTypeRegroupement(null);
        return this;
    }

    public void setRegroupements(Set<Regroupement> regroupements) {
        this.regroupements = regroupements;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeRegroupement)) {
            return false;
        }
        return id != null && id.equals(((TypeRegroupement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeRegroupement{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            "}";
    }
}
