package com.openculture.org.repository;

import com.openculture.org.domain.Abonnement;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Abonnement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    @Query("select ab from Abonnement ab where ab.user.id =:id and ab.statut=true ")
    Optional<Abonnement> findByUserIdAndStatutIsTrue(@Param("id") Long id);
}
