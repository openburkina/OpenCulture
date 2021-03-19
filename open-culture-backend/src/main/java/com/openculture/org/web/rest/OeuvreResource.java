package com.openculture.org.web.rest;

import com.openculture.org.domain.enumeration.TypeFichier;
import com.openculture.org.service.OeuvreService;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.openculture.org.service.dto.OeuvreDTO;

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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.openculture.org.domain.Oeuvre}.
 */
@RestController
@RequestMapping("/api")
public class OeuvreResource {

    private final Logger log = LoggerFactory.getLogger(OeuvreResource.class);

    private static final String ENTITY_NAME = "oeuvre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OeuvreService oeuvreService;

    public OeuvreResource(OeuvreService oeuvreService) {
        this.oeuvreService = oeuvreService;
    }

    /**
     * {@code POST  /oeuvres} : Create a new oeuvre.
     *
     * @param oeuvreDTO the oeuvreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oeuvreDTO, or with status {@code 400 (Bad Request)} if the oeuvre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/oeuvres")
    public ResponseEntity<OeuvreDTO> createOeuvre(@RequestBody OeuvreDTO oeuvreDTO) throws Exception {
        log.debug("REST request to save Oeuvre : {}", oeuvreDTO);
        if (oeuvreDTO.getId() != null) {
            throw new BadRequestAlertException("A new oeuvre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OeuvreDTO result = oeuvreService.save(oeuvreDTO);
        return ResponseEntity.created(new URI("/api/oeuvres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oeuvres} : Updates an existing oeuvre.
     *
     * @param oeuvreDTO the oeuvreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oeuvreDTO,
     * or with status {@code 400 (Bad Request)} if the oeuvreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oeuvreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oeuvres")
    public ResponseEntity<OeuvreDTO> updateOeuvre(@RequestBody OeuvreDTO oeuvreDTO) throws Exception {
        log.debug("REST request to update Oeuvre : {}", oeuvreDTO);
        if (oeuvreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OeuvreDTO result = oeuvreService.save(oeuvreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oeuvreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /oeuvres} : get all the oeuvres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oeuvres in body.
     */
    @GetMapping("/oeuvres/{typeFichier}")
    public ResponseEntity<List<OeuvreDTO>> getAllOeuvres(@PathVariable TypeFichier typeFichier,Pageable pageable) {
        log.debug("REST request to get a page of Oeuvres");
        Page<OeuvreDTO> page = oeuvreService.findAll(typeFichier,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /oeuvres/:id} : get the "id" oeuvre.
     *
     * @param id the id of the oeuvreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oeuvreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/oeuvres/{id}")
    public ResponseEntity<OeuvreDTO> getOeuvre(@PathVariable Long id) {
        log.debug("REST request to get Oeuvre : {}", id);
        OeuvreDTO oeuvreDTO = oeuvreService.findOne(id);
        return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oeuvreDTO.getId().toString()))
        .body(oeuvreDTO);
    }

    /**
     * {@code DELETE  /oeuvres/:id} : delete the "id" oeuvre.
     *
     * @param id the id of the oeuvreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oeuvres/{id}")
    public ResponseEntity<Void> deleteOeuvre(@PathVariable Long id) {
        log.debug("REST request to delete Oeuvre : {}", id);
        oeuvreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @CrossOrigin("http://localhost:8080")
    @GetMapping("/test/{id}")
    public ResponseEntity<Object> getVideo(@PathVariable Long id) {
        return oeuvreService.readMedia(id);
    }


}
