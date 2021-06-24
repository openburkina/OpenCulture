package com.openculture.org.service;

import com.openculture.org.domain.Regroupement;
import com.openculture.org.repository.OeuvreRepository;
import com.openculture.org.repository.RegroupementRepository;
import com.openculture.org.service.dto.RegroupementDTO;
import com.openculture.org.service.mapper.RegroupementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Regroupement}.
 */
@Service
@Transactional
public class RegroupementService {

    private final Logger log = LoggerFactory.getLogger(RegroupementService.class);

    private final RegroupementRepository regroupementRepository;

    private final RegroupementMapper regroupementMapper;

    private final OeuvreRepository oeuvreRepository;

    public RegroupementService(OeuvreRepository oeuvreRepository, RegroupementRepository regroupementRepository, RegroupementMapper regroupementMapper) {
        this.regroupementRepository = regroupementRepository;
        this.regroupementMapper = regroupementMapper;
        this.oeuvreRepository = oeuvreRepository;
    }

    /**
     * Save a regroupement.
     *
     * @param regroupementDTO the entity to save.
     * @return the persisted entity.
     */
    public RegroupementDTO save(RegroupementDTO regroupementDTO) {
        log.debug("Request to save Regroupement : {}", regroupementDTO);
        Regroupement regroupement = regroupementMapper.toEntity(regroupementDTO);
        regroupement = regroupementRepository.save(regroupement);
        return regroupementMapper.toDto(regroupement);
    }

    /**
     * Get all the regroupements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RegroupementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regroupements");
        return regroupementRepository.findAll(pageable)
            .map(regroupementMapper::toDto);
    }


    /**
     * Get one regroupement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RegroupementDTO> findOne(Long id) {
        log.debug("Request to get Regroupement : {}", id);
        return regroupementRepository.findById(id)
            .map(regroupementMapper::toDto);
    }

    /**
     * Delete the regroupement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Regroupement : {}", id);
        if (oeuvreRepository.findAllByRegroupementId(id).isEmpty()) {
            regroupementRepository.deleteById(id);
        } else {
            throw new EntityUsedInAnotherException();
        }
        
    }
}
