package com.openculture.org.repository;

import com.openculture.org.domain.Artiste;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Artiste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtisteRepository extends JpaRepository<Artiste, Long> {
}
