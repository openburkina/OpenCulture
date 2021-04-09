package com.openculture.org.service.dto;

import com.openculture.org.domain.Artiste;
import com.openculture.org.domain.Oeuvre;

public class RechercheDTO {
    private Artiste artiste;
    private Oeuvre oeuvre;

    public RechercheDTO(Artiste artiste, Oeuvre oeuvre) {
        this.artiste = artiste;
        this.oeuvre = oeuvre;
    }

    public RechercheDTO() {
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
    }

    @Override
    public String toString() {
        return "RechercheDTO{" +
            "artiste=" + artiste +
            ", oeuvre=" + oeuvre +
            '}';
    }
}
