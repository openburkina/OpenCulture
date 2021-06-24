package com.openculture.org.web.rest;

import com.openculture.org.domain.ArtisteOeuvre;
import com.openculture.org.service.ArtisteOeuvreService;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.openculture.org.service.dto.ArtisteOeuvreDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.openculture.org.domain.ArtisteOeuvre}.
 */
@RestController
@RequestMapping("/api")
public class ArtisteOeuvreResource {

    private final Logger log = LoggerFactory.getLogger(ArtisteOeuvreResource.class);

    private static final String ENTITY_NAME = "artisteOeuvre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtisteOeuvreService artisteOeuvreService;

    public ArtisteOeuvreResource(ArtisteOeuvreService artisteOeuvreService) {
        this.artisteOeuvreService = artisteOeuvreService;
    }

    /**
     * {@code POST  /artiste-oeuvres} : Create a new artisteOeuvre.
     *
     * @param artisteOeuvreDTO the artisteOeuvreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artisteOeuvreDTO, or with status {@code 400 (Bad Request)} if the artisteOeuvre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artiste-oeuvres")
    public ResponseEntity<ArtisteOeuvreDTO> createArtisteOeuvre(@RequestBody ArtisteOeuvreDTO artisteOeuvreDTO) throws URISyntaxException {
        log.debug("REST request to save ArtisteOeuvre : {}", artisteOeuvreDTO);
        if (artisteOeuvreDTO.getId() != null) {
            throw new BadRequestAlertException("A new artisteOeuvre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtisteOeuvreDTO result = artisteOeuvreService.save(artisteOeuvreDTO);
        return ResponseEntity.created(new URI("/api/artiste-oeuvres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artiste-oeuvres} : Updates an existing artisteOeuvre.
     *
     * @param artisteOeuvreDTO the artisteOeuvreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artisteOeuvreDTO,
     * or with status {@code 400 (Bad Request)} if the artisteOeuvreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artisteOeuvreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artiste-oeuvres")
    public ResponseEntity<ArtisteOeuvreDTO> updateArtisteOeuvre(@RequestBody ArtisteOeuvreDTO artisteOeuvreDTO) throws URISyntaxException {
        log.debug("REST request to update ArtisteOeuvre : {}", artisteOeuvreDTO);
        if (artisteOeuvreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArtisteOeuvreDTO result = artisteOeuvreService.save(artisteOeuvreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, artisteOeuvreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /artiste-oeuvres} : get all the artisteOeuvres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artisteOeuvres in body.
     */
    @GetMapping("/artiste-oeuvres")
    public ResponseEntity<List<ArtisteOeuvreDTO>> getAllArtisteOeuvres(Pageable pageable) {
        log.debug("REST request to get a page of ArtisteOeuvres");
        Page<ArtisteOeuvreDTO> page = artisteOeuvreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /artiste-oeuvres/:id} : get the "id" artisteOeuvre.
     *
     * @param id the id of the artisteOeuvreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artisteOeuvreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artiste-oeuvres/{id}")
    public ResponseEntity<ArtisteOeuvreDTO> getArtisteOeuvre(@PathVariable Long id) {
        log.debug("REST request to get ArtisteOeuvre : {}", id);
        Optional<ArtisteOeuvreDTO> artisteOeuvreDTO = artisteOeuvreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(artisteOeuvreDTO);
    }

    /**
     * {@code DELETE  /artiste-oeuvres/:id} : delete the "id" artisteOeuvre.
     *
     * @param id the id of the artisteOeuvreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artiste-oeuvres/{id}")
    public ResponseEntity<Void> deleteArtisteOeuvre(@PathVariable Long id) {
        log.debug("REST request to delete ArtisteOeuvre : {}", id);
        artisteOeuvreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/artiste-oeuvres/search/{search}/{typeFile}")
    public List<ArtisteOeuvre> onSearch(@PathVariable String search, @PathVariable String typeFile) {
        log.debug("REST request to get a page of Artistes");
        return artisteOeuvreService.onSearch(search,typeFile);
    }
}
