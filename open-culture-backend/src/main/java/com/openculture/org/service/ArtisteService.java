package com.openculture.org.service;

import com.openculture.org.domain.Artiste;
import com.openculture.org.repository.ArtisteRepository;
import com.openculture.org.service.dto.ArtisteDTO;
import com.openculture.org.service.dto.InformationCivilDTO;
import com.openculture.org.service.mapper.ArtisteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Artiste}.
 */
@Service
@Transactional
public class ArtisteService {

    private final Logger log = LoggerFactory.getLogger(ArtisteService.class);

    private final ArtisteRepository artisteRepository;

    private final ArtisteMapper artisteMapper;

    private final InformationCivilService informationCivilService;

    public ArtisteService(ArtisteRepository artisteRepository, ArtisteMapper artisteMapper, InformationCivilService informationCivilService) {
        this.artisteRepository = artisteRepository;
        this.artisteMapper = artisteMapper;
        this.informationCivilService = informationCivilService;
    }

    /**
     * Save a artiste.
     *
     * @param artisteDTO the entity to save.
     * @return the persisted entity.
     */
    public ArtisteDTO save(ArtisteDTO artisteDTO) throws Exception {
        log.debug("Request to save Artiste : {}", artisteDTO);

        if(validedArtiste(artisteDTO) && validedInformationCivil(artisteDTO.getInformationCivilDTO())){
            artisteDTO.setInformationCivilDTO(informationCivilService.save(artisteDTO.getInformationCivilDTO()));
            Artiste artiste = artisteMapper.toEntity(artisteDTO);
            artiste = artisteRepository.save(artiste);
            return artisteMapper.toDto(artiste);
        } else {
            throw new Exception("Certains paramètres concernant l'artiste sont manquants");
        }
    }

    public boolean validedInformationCivil(InformationCivilDTO inf){
        if (inf.getDateNaissance() != null && inf.getLieuNaissance() != null &&
            inf.getNationalite() != null && (inf.getNumeroP() != null || inf.getNumeroS() != null))
            return true;
        return false;
    }

    public boolean validedArtiste(ArtisteDTO art){
        if (art.getNom() != null && art.getPrenom() != null && art.getInformationCivilDTO() != null )
            return true;
        return false;
    }

    /**
     * Get all the artistes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArtisteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Artistes");
        return artisteRepository.findAll(pageable)
            .map(artisteMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<Artiste> onSearch(String search) {
        log.debug("Request to get all Artistes");
        return artisteRepository.findArtisteByCritaria(search);
    }


    /**
     * Get one artiste by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArtisteDTO> findOne(Long id) {
        log.debug("Request to get Artiste : {}", id);
        return artisteRepository.findById(id)
            .map(artisteMapper::toDto);
    }

    /**
     * Delete the artiste by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Artiste : {}", id);
        artisteRepository.deleteById(id);
    }
}
