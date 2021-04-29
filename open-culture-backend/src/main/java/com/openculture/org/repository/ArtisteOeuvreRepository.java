package com.openculture.org.repository;

import com.openculture.org.domain.ArtisteOeuvre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ArtisteOeuvre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtisteOeuvreRepository extends JpaRepository<ArtisteOeuvre, Long> {

    List<ArtisteOeuvre> findByArtisteId(Long id);
    List<ArtisteOeuvre> findAllByOeuvreId(Long id);
    List<ArtisteOeuvre> findAllByArtisteId(Long id);
    void deleteByArtisteId(Long id);

    @Query("select art from ArtisteOeuvre art where("+
        "(:search is null or :search =''" +
        "or UPPER(art.artiste.nom) like upper('%'||:search||'%') or UPPER(art.oeuvre.titre) like upper('%'||:search||'%')"+
        "or UPPER(art.artiste.prenom)like upper('%'||:search||'%')" +
        "or UPPER(art.artiste.nom||' '||art.oeuvre.titre)like upper('%'||:search||'%')" +
        "or UPPER(art.artiste.prenom||' '||art.oeuvre.titre) like upper('%'||:search||'%')" +
        "or UPPER(art.artiste.nom||' '||art.artiste.prenom||' '||art.oeuvre.titre) like upper('%'||:search||'%'))" +
        "and(:typeFile is null or :typeFile ='' or UPPER(art.oeuvre.typeFichier)like upper('%'||:typeFile||'%')) )")
    List<ArtisteOeuvre> findArtisteByCritaria(@Param("search") String search,@Param("typeFile") String typeFile);
}
