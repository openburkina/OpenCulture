package com.openculture.org.service.dto;

public class RechercheDTO {
    private ArtisteDTO artisteDTO;
    private OeuvreDTO oeuvreDTO;

    public ArtisteDTO getArtisteDTO() {
        return artisteDTO;
    }

    public void setArtisteDTO(ArtisteDTO artisteDTO) {
        this.artisteDTO = artisteDTO;
    }

    public OeuvreDTO getOeuvreDTO() {
        return oeuvreDTO;
    }

    public void setOeuvreDTO(OeuvreDTO oeuvreDTO) {
        this.oeuvreDTO = oeuvreDTO;
    }

    @Override
    public String toString() {
        return "RechercheDTO{" +
            "artisteDTO=" + artisteDTO +
            ", oeuvreDTO=" + oeuvreDTO +
            '}';
    }
}
