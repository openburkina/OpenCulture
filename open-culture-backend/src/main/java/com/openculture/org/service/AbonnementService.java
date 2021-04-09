package com.openculture.org.service;

import com.openculture.org.domain.Abonnement;
import com.openculture.org.domain.User;
import com.openculture.org.repository.AbonnementRepository;
import com.openculture.org.repository.UserRepository;
import com.openculture.org.security.SecurityUtils;
import com.openculture.org.service.dto.AbonnementDTO;
import com.openculture.org.service.mapper.AbonnementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Abonnement}.
 */
@Service
@Transactional
public class AbonnementService {

    private final Logger log = LoggerFactory.getLogger(AbonnementService.class);

    private final AbonnementRepository abonnementRepository;

    private final AbonnementMapper abonnementMapper;

    private final UserRepository userRepository;

    public AbonnementService(AbonnementRepository abonnementRepository, AbonnementMapper abonnementMapper, UserRepository userRepository) {
        this.abonnementRepository = abonnementRepository;
        this.abonnementMapper = abonnementMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a abonnement.
     *
     * @param abonnementDTO the entity to save.
     * @return the persisted entity.
     */
    public AbonnementDTO save(AbonnementDTO abonnementDTO) {
        log.debug("Request to save Abonnement : {}", abonnementDTO);
      Optional<User>  user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        abonnementDTO.setDateAbonnement(Instant.now());
        abonnementDTO.setUser(user.get());
        Abonnement abonnement = abonnementMapper.toEntity(abonnementDTO);
        abonnement = abonnementRepository.save(abonnement);
        return abonnementMapper.toDto(abonnement);
    }

    /**
     * Get all the abonnements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AbonnementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Abonnements");
        return abonnementRepository.findAll(pageable)
            .map(abonnementMapper::toDto);
    }


    /**
     * Get one abonnement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AbonnementDTO> findOne(Long id) {
        log.debug("Request to get Abonnement : {}", id);
        return abonnementRepository.findById(id)
            .map(abonnementMapper::toDto);
    }

    /**
     * Delete the abonnement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Abonnement : {}", id);
        abonnementRepository.deleteById(id);
    }
}
