package com.openculture.org.repository;

import com.openculture.org.domain.TypeRegroupement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TypeOeuvre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRegroupementRepository extends JpaRepository<TypeRegroupement, Long> {
}
