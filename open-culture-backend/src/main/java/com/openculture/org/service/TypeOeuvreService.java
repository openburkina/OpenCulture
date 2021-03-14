package com.openculture.org.service;

import com.openculture.org.domain.TypeOeuvre;
import com.openculture.org.repository.TypeOeuvreRepository;
import com.openculture.org.service.dto.TypeOeuvreDTO;
import com.openculture.org.service.mapper.TypeOeuvreMapper;
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
 * Service Implementation for managing {@link TypeOeuvre}.
 */
@Service
@Transactional
public class TypeOeuvreService {

    private final Logger log = LoggerFactory.getLogger(TypeOeuvreService.class);

    private final TypeOeuvreRepository typeOeuvreRepository;

    private final TypeOeuvreMapper typeOeuvreMapper;

    public TypeOeuvreService(TypeOeuvreRepository typeOeuvreRepository, TypeOeuvreMapper typeOeuvreMapper) {
        this.typeOeuvreRepository = typeOeuvreRepository;
        this.typeOeuvreMapper = typeOeuvreMapper;
    }

    /**
     * Save a typeOeuvre.
     *
     * @param typeOeuvreDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeOeuvreDTO save(TypeOeuvreDTO typeOeuvreDTO) {
        log.debug("Request to save TypeOeuvre : {}", typeOeuvreDTO);
        TypeOeuvre typeOeuvre = typeOeuvreMapper.toEntity(typeOeuvreDTO);
        typeOeuvre = typeOeuvreRepository.save(typeOeuvre);
        return typeOeuvreMapper.toDto(typeOeuvre);
    }

    /**
     * Get all the typeOeuvres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeOeuvreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeOeuvres");
        return typeOeuvreRepository.findAll(pageable)
            .map(typeOeuvreMapper::toDto);
    }



    /**
     *  Get all the typeOeuvres where Oeuvre is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<TypeOeuvreDTO> findAllWhereOeuvreIsNull() {
        log.debug("Request to get all typeOeuvres where Oeuvre is null");
        return StreamSupport
            .stream(typeOeuvreRepository.findAll().spliterator(), false)
            .filter(typeOeuvre -> typeOeuvre.getOeuvre() == null)
            .map(typeOeuvreMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one typeOeuvre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeOeuvreDTO> findOne(Long id) {
        log.debug("Request to get TypeOeuvre : {}", id);
        return typeOeuvreRepository.findById(id)
            .map(typeOeuvreMapper::toDto);
    }

    /**
     * Delete the typeOeuvre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeOeuvre : {}", id);
        typeOeuvreRepository.deleteById(id);
    }
}
