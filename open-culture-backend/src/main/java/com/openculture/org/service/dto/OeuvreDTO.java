package com.openculture.org.service.dto;

import javax.persistence.Column;

import com.openculture.org.domain.enumeration.TypeFichier;

import java.time.Instant;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A DTO for the {@link com.openculture.org.domain.Oeuvre} entity.
 */
public class OeuvreDTO implements Serializable {

    private Long id;

    private String titre;

    private Instant dateSortie;

    private Long typeOeuvreId;

    private Long regroupementId;

    private Long artisteId;

    private ArtisteDTO artisteDTO;

    private String resume;

    private String file_name;

    private byte[] file_content;

    private String file_extension;

    private String pathFile;

    private TypeFichier typeFichier;

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

    public TypeFichier getTypeFichier() {
        return typeFichier;
    }

    public void setTypeFichier(TypeFichier typeFichier) {
        this.typeFichier = typeFichier;
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

    public Long getArtisteId() {
        return artisteId;
    }

    public void setArtisteId(Long artisteId) {
        this.artisteId = artisteId;
    }

    public ArtisteDTO getArtisteDTO() {
        return artisteDTO;
    }

    public void setArtisteDTO(ArtisteDTO artisteDTO) {
        this.artisteDTO = artisteDTO;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public byte[] getFile_content() {
        return file_content;
    }

    public void setFile_content(byte[] file_content) {
        this.file_content = file_content;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
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
            "id=" + id +
            ", titre='" + titre + '\'' +
            ", dateSortie=" + dateSortie +
            ", typeOeuvreId=" + typeOeuvreId +
            ", regroupementId=" + regroupementId +
            ", artisteId=" + artisteId +
            ", artisteDTO=" + artisteDTO +
            ", resume='" + resume + '\'' +
            ", file_name='" + file_name + '\'' +
            ", file_content=" + Arrays.toString(file_content) +
            ", file_extension='" + file_extension + '\'' +
            ", pathFile='" + pathFile + '\'' +
            '}';
    }
}
