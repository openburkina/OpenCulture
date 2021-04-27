package com.openculture.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openculture.org.domain.enumeration.TypeFichier;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Oeuvre.
 */
@Entity
@Table(name = "oeuvre")
public class Oeuvre extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "date_sortie")
    private Instant dateSortie;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_content")
    private byte[] fileContent;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "resume")
    private String resume;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_fichier")
    private TypeFichier typeFichier;

    @OneToMany(mappedBy = "oeuvre")
    private Set<ArtisteOeuvre> artisteOeuvres = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "oeuvres", allowSetters = true)
    private Regroupement regroupement;

    @ManyToOne
    @JsonIgnoreProperties(value = "oeuvres", allowSetters = true)
    private TypeOeuvre typeOeuvre;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Oeuvre titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Instant getDateSortie() {
        return dateSortie;
    }

    public Oeuvre dateSortie(Instant dateSortie) {
        this.dateSortie = dateSortie;
        return this;
    }

    public void setDateSortie(Instant dateSortie) {
        this.dateSortie = dateSortie;
    }

    public Set<ArtisteOeuvre> getArtisteOeuvres() {
        return artisteOeuvres;
    }

    public Oeuvre artisteOeuvres(Set<ArtisteOeuvre> artisteOeuvres) {
        this.artisteOeuvres = artisteOeuvres;
        return this;
    }

    public Oeuvre addArtisteOeuvre(ArtisteOeuvre artisteOeuvre) {
        this.artisteOeuvres.add(artisteOeuvre);
        artisteOeuvre.setOeuvre(this);
        return this;
    }

    public Oeuvre removeArtisteOeuvre(ArtisteOeuvre artisteOeuvre) {
        this.artisteOeuvres.remove(artisteOeuvre);
        artisteOeuvre.setOeuvre(null);
        return this;
    }

    public void setArtisteOeuvres(Set<ArtisteOeuvre> artisteOeuvres) {
        this.artisteOeuvres = artisteOeuvres;
    }

    public Regroupement getRegroupement() {
        return regroupement;
    }

    public Oeuvre regroupement(Regroupement regroupement) {
        this.regroupement = regroupement;
        return this;
    }

    public void setRegroupement(Regroupement regroupement) {
        this.regroupement = regroupement;
    }

    public TypeOeuvre getTypeOeuvre() {
        return typeOeuvre;
    }

    public Oeuvre typeOeuvre(TypeOeuvre typeOeuvre) {
        this.typeOeuvre = typeOeuvre;
        return this;
    }

    public void setTypeOeuvre(TypeOeuvre typeOeuvre) {
        this.typeOeuvre = typeOeuvre;
    }

    public TypeFichier getTypeFichier() {
        return typeFichier;
    }

    public void setTypeFichier(TypeFichier typeFichier) {
        this.typeFichier = typeFichier;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Oeuvre)) {
            return false;
        }
        return id != null && id.equals(((Oeuvre) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Oeuvre{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", dateSortie='" + getDateSortie() + "'" +
            "}";
    }
}
