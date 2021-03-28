package com.openculture.org.service.dto;

import com.openculture.org.domain.User;

import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link com.openculture.org.domain.Abonnement} entity.
 */
public class AbonnementDTO implements Serializable {
    
    private Long id;

    private Instant dateAbonnement;

    private String type;

    private User user;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAbonnement() {
        return dateAbonnement;
    }

    public void setDateAbonnement(Instant dateAbonnement) {
        this.dateAbonnement = dateAbonnement;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AbonnementDTO{" +
            "id=" + id +
            ", dateAbonnement=" + dateAbonnement +
            ", type='" + type + '\'' +
            ", user=" + user +
            '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbonnementDTO)) {
            return false;
        }

        return id != null && id.equals(((AbonnementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
