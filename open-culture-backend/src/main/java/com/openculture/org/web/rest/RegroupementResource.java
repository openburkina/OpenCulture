package com.openculture.org.web.rest;

import com.openculture.org.service.RegroupementService;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.openculture.org.service.dto.RegroupementDTO;

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
 * REST controller for managing {@link com.openculture.org.domain.Regroupement}.
 */
@RestController
@RequestMapping("/api")
public class RegroupementResource {

    private final Logger log = LoggerFactory.getLogger(RegroupementResource.class);

    private static final String ENTITY_NAME = "regroupement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegroupementService regroupementService;

    public RegroupementResource(RegroupementService regroupementService) {
        this.regroupementService = regroupementService;
    }

    /**
     * {@code POST  /regroupements} : Create a new regroupement.
     *
     * @param regroupementDTO the regroupementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regroupementDTO, or with status {@code 400 (Bad Request)} if the regroupement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/regroupements")
    public ResponseEntity<RegroupementDTO> createRegroupement(@RequestBody RegroupementDTO regroupementDTO) throws URISyntaxException {
        log.debug("REST request to save Regroupement : {}", regroupementDTO);
        if (regroupementDTO.getId() != null) {
            throw new BadRequestAlertException("A new regroupement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegroupementDTO result = regroupementService.save(regroupementDTO);
        return ResponseEntity.created(new URI("/api/regroupements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /regroupements} : Updates an existing regroupement.
     *
     * @param regroupementDTO the regroupementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regroupementDTO,
     * or with status {@code 400 (Bad Request)} if the regroupementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regroupementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/regroupements")
    public ResponseEntity<RegroupementDTO> updateRegroupement(@RequestBody RegroupementDTO regroupementDTO) throws URISyntaxException {
        log.debug("REST request to update Regroupement : {}", regroupementDTO);
        if (regroupementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegroupementDTO result = regroupementService.save(regroupementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, regroupementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /regroupements} : get all the regroupements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regroupements in body.
     */
    @GetMapping("/regroupements")
    public ResponseEntity<List<RegroupementDTO>> getAllRegroupements(Pageable pageable) {
        log.debug("REST request to get a page of Regroupements");
        Page<RegroupementDTO> page = regroupementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /regroupements/:id} : get the "id" regroupement.
     *
     * @param id the id of the regroupementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regroupementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/regroupements/{id}")
    public ResponseEntity<RegroupementDTO> getRegroupement(@PathVariable Long id) {
        log.debug("REST request to get Regroupement : {}", id);
        Optional<RegroupementDTO> regroupementDTO = regroupementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regroupementDTO);
    }

    /**
     * {@code DELETE  /regroupements/:id} : delete the "id" regroupement.
     *
     * @param id the id of the regroupementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/regroupements/{id}")
    public ResponseEntity<Void> deleteRegroupement(@PathVariable Long id) {
        log.debug("REST request to delete Regroupement : {}", id);
        regroupementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
