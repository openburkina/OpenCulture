package com.openculture.org.repository;

import com.openculture.org.domain.ArtisteOeuvre;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArtisteOeuvre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtisteOeuvreRepository extends JpaRepository<ArtisteOeuvre, Long> {
}
