package com.openculture.org.repository;

import com.openculture.org.domain.TypeOeuvre;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TypeOeuvre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeOeuvreRepository extends JpaRepository<TypeOeuvre, Long> {
}
