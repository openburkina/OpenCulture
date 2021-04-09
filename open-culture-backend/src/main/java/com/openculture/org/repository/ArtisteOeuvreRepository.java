package com.openculture.org.repository;

import com.openculture.org.domain.ArtisteOeuvre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ArtisteOeuvre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtisteOeuvreRepository extends JpaRepository<ArtisteOeuvre, Long> {
    List<ArtisteOeuvre> findAllByOeuvreId(Long id);
    List<ArtisteOeuvre> findAllByArtisteId(Long id);
    void deleteByArtisteId(Long id);
}
