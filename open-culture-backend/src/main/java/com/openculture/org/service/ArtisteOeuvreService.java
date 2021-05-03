package com.openculture.org.service;

import com.openculture.org.domain.ArtisteOeuvre;
import com.openculture.org.repository.ArtisteOeuvreRepository;
import com.openculture.org.service.dto.ArtisteOeuvreDTO;
import com.openculture.org.service.mapper.ArtisteOeuvreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ArtisteOeuvre}.
 */
@Service
@Transactional
public class ArtisteOeuvreService {

    private final Logger log = LoggerFactory.getLogger(ArtisteOeuvreService.class);

    private final ArtisteOeuvreRepository artisteOeuvreRepository;

    private final ArtisteOeuvreMapper artisteOeuvreMapper;

    public ArtisteOeuvreService(ArtisteOeuvreRepository artisteOeuvreRepository, ArtisteOeuvreMapper artisteOeuvreMapper) {
        this.artisteOeuvreRepository = artisteOeuvreRepository;
        this.artisteOeuvreMapper = artisteOeuvreMapper;
    }

    /**
     * Save a artisteOeuvre.
     *
     * @param artisteOeuvreDTO the entity to save.
     * @return the persisted entity.
     */
    public ArtisteOeuvreDTO save(ArtisteOeuvreDTO artisteOeuvreDTO) {
        log.debug("Request to save ArtisteOeuvre : {}", artisteOeuvreDTO);
        ArtisteOeuvre artisteOeuvre = artisteOeuvreMapper.toEntity(artisteOeuvreDTO);
        artisteOeuvre = artisteOeuvreRepository.save(artisteOeuvre);
        return artisteOeuvreMapper.toDto(artisteOeuvre);
    }

    /**
     * Get all the artisteOeuvres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArtisteOeuvreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArtisteOeuvres");
        return artisteOeuvreRepository.findAll(pageable)
            .map(artisteOeuvreMapper::toDto);
    }


    /**
     * Get one artisteOeuvre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArtisteOeuvreDTO> findOne(Long id) {
        log.debug("Request to get ArtisteOeuvre : {}", id);
        return artisteOeuvreRepository.findById(id)
            .map(artisteOeuvreMapper::toDto);
    }

    /**
     * Delete the artisteOeuvre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArtisteOeuvre : {}", id);
        artisteOeuvreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ArtisteOeuvre> findByArtisteId(Long id) {
        log.debug("Request to get all ArtisteOeuvres");
        return artisteOeuvreRepository.findByArtisteId(id);
    }

    @Transactional(readOnly = true)
    public List<ArtisteOeuvre> onSearch(String search,String typeFile) {
        log.debug("Request to get all ArtisteOeuvres");
       if (search.equals("null")){
           search = null;
       } else {
           search.trim();
       }

        if (typeFile.equals("null")){
            typeFile = null;
        }
        return artisteOeuvreRepository.findArtisteByCritaria(search,typeFile);
    }
}
