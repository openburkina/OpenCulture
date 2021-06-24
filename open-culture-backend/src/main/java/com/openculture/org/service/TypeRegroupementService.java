package com.openculture.org.service;

import com.openculture.org.domain.TypeRegroupement;
import com.openculture.org.repository.RegroupementRepository;
import com.openculture.org.repository.TypeRegroupementRepository;
import com.openculture.org.service.dto.TypeRegroupementDTO;
import com.openculture.org.service.mapper.TypeRegroupementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TypeRegroupement}.
 */
@Service
@Transactional
public class TypeRegroupementService {

    private final Logger log = LoggerFactory.getLogger(TypeRegroupementService.class);

    private final TypeRegroupementRepository typeRegroupementRepository;

    private final TypeRegroupementMapper typeRegroupementMapper;

    private final RegroupementRepository regroupementRepository;

    public TypeRegroupementService(RegroupementRepository regroupementRepository, TypeRegroupementRepository typeRegroupementRepository, TypeRegroupementMapper typeRegroupementMapper) {
        this.typeRegroupementRepository = typeRegroupementRepository;
        this.typeRegroupementMapper = typeRegroupementMapper;
        this.regroupementRepository = regroupementRepository;
    }

    /**
     * Save a typeRegroupement.
     *
     * @param typeRegroupementDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeRegroupementDTO save(TypeRegroupementDTO typeRegroupementDTO) {
        log.debug("Request to save TypeRegroupement : {}", typeRegroupementDTO);

        TypeRegroupement typeRegroupement = typeRegroupementMapper.toEntity(typeRegroupementDTO);
        typeRegroupement = typeRegroupementRepository.save(typeRegroupement);
        return typeRegroupementMapper.toDto(typeRegroupement);
    }

    /**
     * Get all the typeRegroupements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeRegroupementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeRegroupements");
        return typeRegroupementRepository.findAll(pageable)
            .map(typeRegroupementMapper::toDto);
    }

    /**
     * Get one typeRegroupement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeRegroupementDTO> findOne(Long id) {
        log.debug("Request to get TypeRegroupement : {}", id);
        return typeRegroupementRepository.findById(id)
            .map(typeRegroupementMapper::toDto);
    }

    /**
     * Delete the typeRegroupement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeRegroupement : {}", id);
        if (regroupementRepository.findAllByTypeRegroupementId(id).isEmpty()) {
            typeRegroupementRepository.deleteById(id);
        } else {
            throw new EntityUsedInAnotherException();
        }
    }
}
