package com.openculture.org.web.rest;

import com.openculture.org.domain.Abonnement;
import com.openculture.org.domain.User;
import com.openculture.org.service.AbonnementService;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.openculture.org.service.dto.AbonnementDTO;

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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.openculture.org.domain.Abonnement}.
 */
@RestController
@RequestMapping("/api")
public class AbonnementResource {

    private final Logger log = LoggerFactory.getLogger(AbonnementResource.class);

    private static final String ENTITY_NAME = "abonnement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AbonnementService abonnementService;

    public AbonnementResource(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;
    }

    /**
     * {@code POST  /abonnements} : Create a new abonnement.
     *
     * @param abonnementDTO the abonnementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new abonnementDTO, or with status {@code 400 (Bad Request)} if the abonnement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/abonnements")
    public ResponseEntity<AbonnementDTO> createAbonnement(@RequestBody AbonnementDTO abonnementDTO) throws URISyntaxException {
        log.debug("REST request to save Abonnement : {}", abonnementDTO);
        if (abonnementDTO.getId() != null) {
            throw new BadRequestAlertException("A new abonnement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AbonnementDTO result = abonnementService.save(abonnementDTO);
        return ResponseEntity.created(new URI("/api/abonnements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /abonnements} : Updates an existing abonnement.
     *
     * @param abonnementDTO the abonnementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonnementDTO,
     * or with status {@code 400 (Bad Request)} if the abonnementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the abonnementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/abonnements")
    public ResponseEntity<AbonnementDTO> updateAbonnement(@RequestBody AbonnementDTO abonnementDTO) throws URISyntaxException {
        log.debug("REST request to update Abonnement : {}", abonnementDTO);
        if (abonnementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AbonnementDTO result = abonnementService.save(abonnementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, abonnementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /abonnements} : get all the abonnements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abonnements in body.
     */
    @GetMapping("/abonnements")
    public ResponseEntity<List<AbonnementDTO>> getAllAbonnements(Pageable pageable) {
        log.debug("REST request to get a page of Abonnements");
        Page<AbonnementDTO> page = abonnementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /abonnements/:id} : get the "id" abonnement.
     *
     * @param id the id of the abonnementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abonnementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/abonnements/{id}")
    public ResponseEntity<AbonnementDTO> getAbonnement(@PathVariable Long id) {
        log.debug("REST request to get Abonnement : {}", id);
        Optional<AbonnementDTO> abonnementDTO = abonnementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(abonnementDTO);
    }

    /**
     * {@code DELETE  /abonnements/:id} : delete the "id" abonnement.
     *
     * @param id the id of the abonnementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/abonnements/{id}")
    public ResponseEntity<Void> deleteAbonnement(@PathVariable Long id) {
        log.debug("REST request to delete Abonnement : {}", id);
        abonnementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/abonnements/user-id/{id}")
    public AbonnementDTO findByUserId(@PathVariable Long id) {
        log.debug("REST request to get Abonnement : {}", id);
        AbonnementDTO abonnementDTO = abonnementService.findByUserId(id);
        return  abonnementDTO;
    }

    @GetMapping("/abonnements/send-email")
    public User sendEmail() throws IOException {
        return abonnementService.sendEmail();
    }
}
