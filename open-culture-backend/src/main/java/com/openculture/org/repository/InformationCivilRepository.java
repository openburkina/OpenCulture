package com.openculture.org.repository;

import com.openculture.org.domain.InformationCivil;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InformationCivil entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformationCivilRepository extends JpaRepository<InformationCivil, Long> {
}
