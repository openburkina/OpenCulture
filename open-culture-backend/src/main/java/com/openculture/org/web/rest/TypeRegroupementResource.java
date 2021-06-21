package com.openculture.org.web.rest;

import com.openculture.org.service.TypeRegroupementService;
import com.openculture.org.service.dto.TypeRegroupementDTO;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.openculture.org.domain.TypeRegroupement}.
 */
@RestController
@RequestMapping("/api")
public class TypeRegroupementResource {

    private final Logger log = LoggerFactory.getLogger(TypeRegroupementResource.class);

    private static final String ENTITY_NAME = "typeRegroupement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeRegroupementService typeRegroupementService;

    public TypeRegroupementResource(TypeRegroupementService typeRegroupementService) {
        this.typeRegroupementService = typeRegroupementService;
    }

    /**
     * {@code POST  /type-regroupements} : Create a new typeRegroupement.
     *
     * @param typeRegroupementDTO the typeRegroupementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeRegroupementDTO, or with status {@code 400 (Bad Request)} if the typeRegroupement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-regroupements")
    public ResponseEntity<TypeRegroupementDTO> createTypeRegroupement(@RequestBody TypeRegroupementDTO typeRegroupementDTO) throws URISyntaxException {
        log.debug("REST request to save TypeRegroupement : {}", typeRegroupementDTO);
        if (typeRegroupementDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeRegroupement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeRegroupementDTO result = typeRegroupementService.save(typeRegroupementDTO);
        return ResponseEntity.created(new URI("/api/type-regroupements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-regroupements} : Updates an existing typeRegroupement.
     *
     * @param typeRegroupementDTO the typeRegroupementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRegroupementDTO,
     * or with status {@code 400 (Bad Request)} if the typeRegroupementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeRegroupementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-regroupements")
    public ResponseEntity<TypeRegroupementDTO> updateTypeRegroupement(@RequestBody TypeRegroupementDTO typeRegroupementDTO) throws URISyntaxException {
        log.debug("REST request to update TypeRegroupement : {}", typeRegroupementDTO);
        if (typeRegroupementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeRegroupementDTO result = typeRegroupementService.save(typeRegroupementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeRegroupementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-regroupements} : get all the typeRegroupements.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeRegroupements in body.
     */
    @GetMapping("/type-regroupements")
    public ResponseEntity<List<TypeRegroupementDTO>> getAllTypeRegroupements(Pageable pageable, @RequestParam(required = false) String filter) {
        log.debug("REST request to get a page of TypeRegroupements");
        Page<TypeRegroupementDTO> page = typeRegroupementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-regroupements/:id} : get the "id" typeRegroupement.
     *
     * @param id the id of the typeRegroupementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeRegroupementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-regroupements/{id}")
    public ResponseEntity<TypeRegroupementDTO> getTypeRegroupement(@PathVariable Long id) {
        log.debug("REST request to get TypeRegroupement : {}", id);
        Optional<TypeRegroupementDTO> typeRegroupementDTO = typeRegroupementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeRegroupementDTO);
    }

    /**
     * {@code DELETE  /type-regroupements/:id} : delete the "id" typeRegroupement.
     *
     * @param id the id of the typeRegroupementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-regroupements/{id}")
    public ResponseEntity<Void> deleteTypeRegroupement(@PathVariable Long id) {
        log.debug("REST request to delete TypeRegroupement : {}", id);
        typeRegroupementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
