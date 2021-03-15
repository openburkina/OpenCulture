package com.openculture.org.service;

import com.openculture.org.domain.Oeuvre;
import com.openculture.org.repository.OeuvreRepository;
import com.openculture.org.service.dto.OeuvreDTO;
import com.openculture.org.service.mapper.OeuvreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Oeuvre}.
 */
@Service
@Transactional
public class OeuvreService {

    private final Logger log = LoggerFactory.getLogger(OeuvreService.class);

    private final OeuvreRepository oeuvreRepository;

    private final OeuvreMapper oeuvreMapper;

    public OeuvreService(OeuvreRepository oeuvreRepository, OeuvreMapper oeuvreMapper) {
        this.oeuvreRepository = oeuvreRepository;
        this.oeuvreMapper = oeuvreMapper;
    }

    /**
     * Save a oeuvre.
     *
     * @param oeuvreDTO the entity to save.
     * @return the persisted entity.
     */
    public OeuvreDTO save(OeuvreDTO oeuvreDTO) {
        log.debug("Request to save Oeuvre : {}", oeuvreDTO);
        Oeuvre oeuvre = oeuvreMapper.toEntity(oeuvreDTO);
        oeuvre = oeuvreRepository.save(oeuvre);
        return oeuvreMapper.toDto(oeuvre);
    }

    /**
     * Get all the oeuvres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Oeuvres");
        return oeuvreRepository.findAll(pageable)
            .map(oeuvreMapper::toDto);
    }


    /**
     * Get one oeuvre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OeuvreDTO> findOne(Long id) {
        log.debug("Request to get Oeuvre : {}", id);
        return oeuvreRepository.findById(id)
            .map(oeuvreMapper::toDto);
    }

    /**
     * Delete the oeuvre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Oeuvre : {}", id);
        oeuvreRepository.deleteById(id);
    }
}
