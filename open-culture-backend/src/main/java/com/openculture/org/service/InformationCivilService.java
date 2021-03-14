package com.openculture.org.service;

import com.openculture.org.domain.InformationCivil;
import com.openculture.org.repository.InformationCivilRepository;
import com.openculture.org.service.dto.InformationCivilDTO;
import com.openculture.org.service.mapper.InformationCivilMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link InformationCivil}.
 */
@Service
@Transactional
public class InformationCivilService {

    private final Logger log = LoggerFactory.getLogger(InformationCivilService.class);

    private final InformationCivilRepository informationCivilRepository;

    private final InformationCivilMapper informationCivilMapper;

    public InformationCivilService(InformationCivilRepository informationCivilRepository, InformationCivilMapper informationCivilMapper) {
        this.informationCivilRepository = informationCivilRepository;
        this.informationCivilMapper = informationCivilMapper;
    }

    /**
     * Save a informationCivil.
     *
     * @param informationCivilDTO the entity to save.
     * @return the persisted entity.
     */
    public InformationCivilDTO save(InformationCivilDTO informationCivilDTO) {
        log.debug("Request to save InformationCivil : {}", informationCivilDTO);
        InformationCivil informationCivil = informationCivilMapper.toEntity(informationCivilDTO);
        informationCivil = informationCivilRepository.save(informationCivil);
        return informationCivilMapper.toDto(informationCivil);
    }

    /**
     * Get all the informationCivils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InformationCivilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InformationCivils");
        return informationCivilRepository.findAll(pageable)
            .map(informationCivilMapper::toDto);
    }



    /**
     *  Get all the informationCivils where Artiste is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<InformationCivilDTO> findAllWhereArtisteIsNull() {
        log.debug("Request to get all informationCivils where Artiste is null");
        return StreamSupport
            .stream(informationCivilRepository.findAll().spliterator(), false)
            .filter(informationCivil -> informationCivil.getArtiste() == null)
            .map(informationCivilMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one informationCivil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InformationCivilDTO> findOne(Long id) {
        log.debug("Request to get InformationCivil : {}", id);
        return informationCivilRepository.findById(id)
            .map(informationCivilMapper::toDto);
    }

    /**
     * Delete the informationCivil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InformationCivil : {}", id);
        informationCivilRepository.deleteById(id);
    }
}
