package com.openculture.org.service.dto;

import javax.persistence.Column;

import com.openculture.org.domain.enumeration.TypeFichier;

import java.time.Instant;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A DTO for the {@link com.openculture.org.domain.Oeuvre} entity.
 */
public class OeuvreDTO implements Serializable {

    private Long id;

    private String titre;

    private Instant dateSortie;

    private Long typeOeuvreId;

    private Long regroupementId;

    private TypeOeuvreDTO typeOeuvreDTO;

    private RegroupementDTO regroupementDTO;

    private Long artisteId;

    private List<ArtisteDTO> artistes;

    private ArtisteDTO artisteDTO;

    private String resume;

    private String fileName;

    private byte[] fileContent;

    private String fileExtension;

    private String nomArtiste;

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

    public void setTypeOeuvreDTO(TypeOeuvreDTO typeOeuvreDTO) {
        this.typeOeuvreDTO = typeOeuvreDTO;
    }

    public TypeOeuvreDTO getTypeOeuvreDTO() {
        return typeOeuvreDTO;
    }

    public void setRegroupementDTO(RegroupementDTO regroupementDTO) {
        this.regroupementDTO = regroupementDTO;
    }

    public RegroupementDTO getRegroupementDTO() {
        return regroupementDTO;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getNomArtiste() {
        return nomArtiste;
    }

    public void setNomArtiste(String nomArtiste) {
        this.nomArtiste = nomArtiste;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public List<ArtisteDTO> getArtistes() {
        return artistes;
    }

    public void setArtistes(List<ArtisteDTO> artistes) {
        this.artistes = artistes;
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
            ", fileName='" + fileName + '\'' +
            ", fileContent=" + Arrays.toString(fileContent) +
            ", fileExtension='" + fileExtension + '\'' +
            ", nomArtiste='" + nomArtiste + '\'' +
            ", pathFile='" + pathFile + '\'' +
            ", typeFichier=" + typeFichier +
            '}';
    }
}
