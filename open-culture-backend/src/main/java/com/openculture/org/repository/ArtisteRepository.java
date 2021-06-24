package com.openculture.org.repository;

import com.openculture.org.domain.Artiste;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Artiste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtisteRepository extends JpaRepository<Artiste, Long> {

  /*  @Query("select art from Artiste art where("+
     ":search is null or :search ='' or UPPER(art.nom) like upper('%'||:search||'%')"+
      "or UPPER(art.prenom) like upper('%'||:search||'%'))")
    List<Artiste> findArtisteByCritaria(@Param("search") String search); */
}
