package com.openculture.org.web.rest;

import com.openculture.org.service.InformationCivilService;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.openculture.org.service.dto.InformationCivilDTO;

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
 * REST controller for managing {@link com.openculture.org.domain.InformationCivil}.
 */
@RestController
@RequestMapping("/api")
public class InformationCivilResource {

    private final Logger log = LoggerFactory.getLogger(InformationCivilResource.class);

    private static final String ENTITY_NAME = "informationCivil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InformationCivilService informationCivilService;

    public InformationCivilResource(InformationCivilService informationCivilService) {
        this.informationCivilService = informationCivilService;
    }

    /**
     * {@code POST  /information-civils} : Create a new informationCivil.
     *
     * @param informationCivilDTO the informationCivilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new informationCivilDTO, or with status {@code 400 (Bad Request)} if the informationCivil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/information-civils")
    public ResponseEntity<InformationCivilDTO> createInformationCivil(@RequestBody InformationCivilDTO informationCivilDTO) throws URISyntaxException {
        log.debug("REST request to save InformationCivil : {}", informationCivilDTO);
        if (informationCivilDTO.getId() != null) {
            throw new BadRequestAlertException("A new informationCivil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InformationCivilDTO result = informationCivilService.save(informationCivilDTO);
        return ResponseEntity.created(new URI("/api/information-civils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /information-civils} : Updates an existing informationCivil.
     *
     * @param informationCivilDTO the informationCivilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informationCivilDTO,
     * or with status {@code 400 (Bad Request)} if the informationCivilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the informationCivilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/information-civils")
    public ResponseEntity<InformationCivilDTO> updateInformationCivil(@RequestBody InformationCivilDTO informationCivilDTO) throws URISyntaxException {
        log.debug("REST request to update InformationCivil : {}", informationCivilDTO);
        if (informationCivilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InformationCivilDTO result = informationCivilService.save(informationCivilDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, informationCivilDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /information-civils} : get all the informationCivils.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of informationCivils in body.
     */
    @GetMapping("/information-civils")
    public ResponseEntity<List<InformationCivilDTO>> getAllInformationCivils(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("artiste-is-null".equals(filter)) {
            log.debug("REST request to get all InformationCivils where artiste is null");
            return new ResponseEntity<>(informationCivilService.findAllWhereArtisteIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of InformationCivils");
        Page<InformationCivilDTO> page = informationCivilService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /information-civils/:id} : get the "id" informationCivil.
     *
     * @param id the id of the informationCivilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informationCivilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/information-civils/{id}")
    public ResponseEntity<InformationCivilDTO> getInformationCivil(@PathVariable Long id) {
        log.debug("REST request to get InformationCivil : {}", id);
        Optional<InformationCivilDTO> informationCivilDTO = informationCivilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(informationCivilDTO);
    }

    /**
     * {@code DELETE  /information-civils/:id} : delete the "id" informationCivil.
     *
     * @param id the id of the informationCivilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/information-civils/{id}")
    public ResponseEntity<Void> deleteInformationCivil(@PathVariable Long id) {
        log.debug("REST request to delete InformationCivil : {}", id);
        informationCivilService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
