package com.openculture.org.repository;

import com.openculture.org.domain.Oeuvre;
import com.openculture.org.domain.Regroupement;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Regroupement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegroupementRepository extends JpaRepository<Regroupement, Long> {
    List<Oeuvre> findAllByTypeRegroupementId(Long id);

}
