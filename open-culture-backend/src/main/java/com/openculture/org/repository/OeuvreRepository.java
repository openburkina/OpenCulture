package com.openculture.org.repository;

import java.util.List;

import com.openculture.org.domain.Oeuvre;
import com.openculture.org.domain.enumeration.TypeFichier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Oeuvre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OeuvreRepository extends JpaRepository<Oeuvre, Long> {
    Page<Oeuvre> findAllByTypeFichier(Pageable pageable,TypeFichier typeFichier);
    List<Oeuvre> findAllByRegroupementId(Long id);
    List<Oeuvre> findAllByTypeOeuvreId(Long id);
    List<Oeuvre> findTop5ByTypeOeuvreIntituleAndCreatedByOrderByCreatedDateDesc(String categorie, String user);
    List<Oeuvre> findTop5ByTypeOeuvreIntituleOrderByCreatedDateDesc(String categorie);
    
}
