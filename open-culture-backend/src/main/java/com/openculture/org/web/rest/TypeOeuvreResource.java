package com.openculture.org.web.rest;

import com.openculture.org.service.TypeOeuvreService;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.openculture.org.service.dto.TypeOeuvreDTO;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.openculture.org.domain.TypeOeuvre}.
 */
@RestController
@RequestMapping("/api")
public class TypeOeuvreResource {

    private final Logger log = LoggerFactory.getLogger(TypeOeuvreResource.class);

    private static final String ENTITY_NAME = "typeOeuvre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeOeuvreService typeOeuvreService;

    public TypeOeuvreResource(TypeOeuvreService typeOeuvreService) {
        this.typeOeuvreService = typeOeuvreService;
    }

    /**
     * {@code POST  /type-oeuvres} : Create a new typeOeuvre.
     *
     * @param typeOeuvreDTO the typeOeuvreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeOeuvreDTO, or with status {@code 400 (Bad Request)} if the typeOeuvre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-oeuvres")
    public ResponseEntity<TypeOeuvreDTO> createTypeOeuvre(@RequestBody TypeOeuvreDTO typeOeuvreDTO) throws URISyntaxException {
        log.debug("REST request to save TypeOeuvre : {}", typeOeuvreDTO);
        if (typeOeuvreDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeOeuvre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeOeuvreDTO result = typeOeuvreService.save(typeOeuvreDTO);
        return ResponseEntity.created(new URI("/api/type-oeuvres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-oeuvres} : Updates an existing typeOeuvre.
     *
     * @param typeOeuvreDTO the typeOeuvreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOeuvreDTO,
     * or with status {@code 400 (Bad Request)} if the typeOeuvreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOeuvreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-oeuvres")
    public ResponseEntity<TypeOeuvreDTO> updateTypeOeuvre(@RequestBody TypeOeuvreDTO typeOeuvreDTO) throws URISyntaxException {
        log.debug("REST request to update TypeOeuvre : {}", typeOeuvreDTO);
        if (typeOeuvreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeOeuvreDTO result = typeOeuvreService.save(typeOeuvreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeOeuvreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-oeuvres} : get all the typeOeuvres.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeOeuvres in body.
     */
    @GetMapping("/type-oeuvres")
    public ResponseEntity<List<TypeOeuvreDTO>> getAllTypeOeuvres(Pageable pageable, @RequestParam(required = false) String filter) {
        // if ("oeuvre-is-null".equals(filter)) {
        //     log.debug("REST request to get all TypeOeuvres where oeuvre is null");
        //     return new ResponseEntity<>(typeOeuvreService.findAllWhereOeuvreIsNull(),
        //             HttpStatus.OK);
        // }
        log.debug("REST request to get a page of TypeOeuvres");
        Page<TypeOeuvreDTO> page = typeOeuvreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-oeuvres/:id} : get the "id" typeOeuvre.
     *
     * @param id the id of the typeOeuvreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeOeuvreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-oeuvres/{id}")
    public ResponseEntity<TypeOeuvreDTO> getTypeOeuvre(@PathVariable Long id) {
        log.debug("REST request to get TypeOeuvre : {}", id);
        Optional<TypeOeuvreDTO> typeOeuvreDTO = typeOeuvreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOeuvreDTO);
    }

    /**
     * {@code DELETE  /type-oeuvres/:id} : delete the "id" typeOeuvre.
     *
     * @param id the id of the typeOeuvreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-oeuvres/{id}")
    public ResponseEntity<Void> deleteTypeOeuvre(@PathVariable Long id) {
        log.debug("REST request to delete TypeOeuvre : {}", id);
        typeOeuvreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
