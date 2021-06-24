package com.openculture.org.service;

import com.openculture.org.domain.Artiste;
import com.openculture.org.domain.ArtisteOeuvre;
import com.openculture.org.repository.ArtisteOeuvreRepository;
import com.openculture.org.domain.Oeuvre;
import com.openculture.org.repository.ArtisteRepository;
import com.openculture.org.repository.OeuvreRepository;
import com.openculture.org.service.dto.ArtisteDTO;
import com.openculture.org.service.dto.InformationCivilDTO;
import com.openculture.org.service.dto.OeuvreDTO;
import com.openculture.org.service.dto.RechercheDTO;
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

    private final ArtisteOeuvreService artisteOeuvreService;

    private final OeuvreRepository oeuvreRepository;

    private final InformationCivilService informationCivilService;
   private List<Artiste> artistes;
   private List<ArtisteOeuvre> artisteOeuvres ;
    Optional<Oeuvre> oeuvres ;
    RechercheDTO rechercheDTOS;

    private final ArtisteOeuvreRepository artisteOeuvreRepository;

    public ArtisteService(ArtisteOeuvreRepository artisteOeuvreRepository,ArtisteRepository artisteRepository, ArtisteMapper artisteMapper, InformationCivilService informationCivilService, ArtisteOeuvreService artisteOeuvreService, OeuvreRepository oeuvreRepository) {

        this.artisteRepository = artisteRepository;
        this.artisteOeuvreRepository = artisteOeuvreRepository;
        this.artisteMapper = artisteMapper;
        this.informationCivilService = informationCivilService;
        this.artisteOeuvreService = artisteOeuvreService;
        this.oeuvreRepository = oeuvreRepository;
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

  /*  @Transactional(readOnly = true)
    public RechercheDTO onSearch(String search) {
        log.debug("Request to get all Artistes");
        artistes = artisteRepository.findArtisteByCritaria(search);
        if (artistes.size()>0) {
          artistes.forEach(artiste ->{
              this.rechercheDTOS = new RechercheDTO();
              this.rechercheDTOS.setArtiste(artiste);
              artisteOeuvres = artisteOeuvreService.findByArtisteId(artiste.getId());
          });
         if (artisteOeuvres.size()>0){
             artisteOeuvres.forEach(artisteOeuvre -> {
                 System.out.println("====ID==== "+artisteOeuvre.getArtiste().getId());
                 this.rechercheDTOS = new RechercheDTO();
                this.oeuvres = oeuvreRepository.findById(artisteOeuvre.getOeuvre().getId());
                 this.rechercheDTOS.setOeuvre(this.oeuvres.get());
             });
         }
        }
        return this.rechercheDTOS;
    } */


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
     * @throws Exception
     */
    public void delete(Long id) {
        log.debug("Request to delete Artiste : {}", id);
        List<ArtisteOeuvre> artOeuvres = artisteOeuvreRepository.findAllByArtisteId(id);
        if (artOeuvres.isEmpty()) {
            informationCivilService.delete(this.findOne(id).get().getInformationCivilDTO().getId());
            artisteRepository.deleteById(id);
        } else {
            throw new EntityUsedInAnotherException();
        }
        
    }
}
