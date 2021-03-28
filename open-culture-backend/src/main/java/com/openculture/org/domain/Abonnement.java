package com.openculture.org.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Abonnement.
 */
@Entity
@Table(name = "abonnement")
public class Abonnement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_abonnement")
    private Instant dateAbonnement;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JsonIgnoreProperties(value = "abonnements", allowSetters = true)
    private User user;
    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAbonnement() {
        return dateAbonnement;
    }

    public Abonnement dateAbonnement(Instant dateAbonnement) {
        this.dateAbonnement = dateAbonnement;
        return this;
    }

    public void setDateAbonnement(Instant dateAbonnement) {
        this.dateAbonnement = dateAbonnement;
    }

    public String getType() {
        return type;
    }

    public Abonnement type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Abonnement)) {
            return false;
        }
        return id != null && id.equals(((Abonnement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
            "id=" + id +
            ", dateAbonnement=" + dateAbonnement +
            ", type='" + type + '\'' +
            ", user=" + user +
            '}';
    }
}
