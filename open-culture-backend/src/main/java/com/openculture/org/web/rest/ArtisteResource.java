package com.openculture.org.web.rest;

import com.openculture.org.domain.Artiste;
import com.openculture.org.service.ArtisteService;
import com.openculture.org.service.dto.RechercheDTO;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.openculture.org.service.dto.ArtisteDTO;

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
 * REST controller for managing {@link com.openculture.org.domain.Artiste}.
 */
@RestController
@RequestMapping("/api")
public class ArtisteResource {

    private final Logger log = LoggerFactory.getLogger(ArtisteResource.class);

    private static final String ENTITY_NAME = "artiste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtisteService artisteService;

    public ArtisteResource(ArtisteService artisteService) {
        this.artisteService = artisteService;
    }

    /**
     * {@code POST  /artistes} : Create a new artiste.
     *
     * @param artisteDTO the artisteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artisteDTO, or with status {@code 400 (Bad Request)} if the artiste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artistes")
    public ResponseEntity<ArtisteDTO> createArtiste(@RequestBody ArtisteDTO artisteDTO) throws Exception {
        log.debug("REST request to save Artiste : {}", artisteDTO);
        if (artisteDTO.getId() != null) {
            throw new BadRequestAlertException("A new artiste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtisteDTO result = artisteService.save(artisteDTO);
        return ResponseEntity.created(new URI("/api/artistes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artistes} : Updates an existing artiste.
     *
     * @param artisteDTO the artisteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artisteDTO,
     * or with status {@code 400 (Bad Request)} if the artisteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artisteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artistes")
    public ResponseEntity<ArtisteDTO> updateArtiste(@RequestBody ArtisteDTO artisteDTO) throws Exception {
        log.debug("REST request to update Artiste : {}", artisteDTO);
        if (artisteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArtisteDTO result = artisteService.save(artisteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, artisteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /artistes} : get all the artistes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artistes in body.
     */
    @GetMapping("/artistes")
    public ResponseEntity<List<ArtisteDTO>> getAllArtistes(Pageable pageable) {
        log.debug("REST request to get a page of Artistes");
        Page<ArtisteDTO> page = artisteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /artistes/:id} : get the "id" artiste.
     *
     * @param id the id of the artisteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artisteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artistes/{id}")
    public ResponseEntity<ArtisteDTO> getArtiste(@PathVariable Long id) {
        log.debug("REST request to get Artiste : {}", id);
        Optional<ArtisteDTO> artisteDTO = artisteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(artisteDTO);
    }

    /**
     * {@code DELETE  /artistes/:id} : delete the "id" artiste.
     *
     * @param id the id of the artisteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artistes/{id}")
    public ResponseEntity<Void> deleteArtiste(@PathVariable Long id) {
        log.debug("REST request to delete Artiste : {}", id);
        artisteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

 /*   @GetMapping("/artistes/search/{search}")
    public RechercheDTO onSearch(@PathVariable String search) {
        log.debug("REST request to get a page of Artistes");
        return artisteService.onSearch(search);
    } */
}
