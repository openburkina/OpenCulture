package com.openculture.org.web.rest;

import com.openculture.org.OpencultureApp;
import com.openculture.org.domain.Abonnement;
import com.openculture.org.repository.AbonnementRepository;
import com.openculture.org.service.AbonnementService;
import com.openculture.org.service.dto.AbonnementDTO;
import com.openculture.org.service.mapper.AbonnementMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AbonnementResource} REST controller.
 */
@SpringBootTest(classes = OpencultureApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AbonnementResourceIT {

    private static final Instant DEFAULT_DATE_ABONNEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ABONNEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private AbonnementMapper abonnementMapper;

    @Autowired
    private AbonnementService abonnementService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbonnementMockMvc;

    private Abonnement abonnement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonnement createEntity(EntityManager em) {
        Abonnement abonnement = new Abonnement()
            .dateAbonnement(DEFAULT_DATE_ABONNEMENT)
            .type(DEFAULT_TYPE);
        return abonnement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonnement createUpdatedEntity(EntityManager em) {
        Abonnement abonnement = new Abonnement()
            .dateAbonnement(UPDATED_DATE_ABONNEMENT)
            .type(UPDATED_TYPE);
        return abonnement;
    }

    @BeforeEach
    public void initTest() {
        abonnement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAbonnement() throws Exception {
        int databaseSizeBeforeCreate = abonnementRepository.findAll().size();
        // Create the Abonnement
        AbonnementDTO abonnementDTO = abonnementMapper.toDto(abonnement);
        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnementDTO)))
            .andExpect(status().isCreated());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeCreate + 1);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getDateAbonnement()).isEqualTo(DEFAULT_DATE_ABONNEMENT);
        assertThat(testAbonnement.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createAbonnementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = abonnementRepository.findAll().size();

        // Create the Abonnement with an existing ID
        abonnement.setId(1L);
        AbonnementDTO abonnementDTO = abonnementMapper.toDto(abonnement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonnementMockMvc.perform(post("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAbonnements() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        // Get all the abonnementList
        restAbonnementMockMvc.perform(get("/api/abonnements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonnement.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAbonnement").value(hasItem(DEFAULT_DATE_ABONNEMENT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getAbonnement() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        // Get the abonnement
        restAbonnementMockMvc.perform(get("/api/abonnements/{id}", abonnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abonnement.getId().intValue()))
            .andExpect(jsonPath("$.dateAbonnement").value(DEFAULT_DATE_ABONNEMENT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }
    @Test
    @Transactional
    public void getNonExistingAbonnement() throws Exception {
        // Get the abonnement
        restAbonnementMockMvc.perform(get("/api/abonnements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbonnement() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();

        // Update the abonnement
        Abonnement updatedAbonnement = abonnementRepository.findById(abonnement.getId()).get();
        // Disconnect from session so that the updates on updatedAbonnement are not directly saved in db
        em.detach(updatedAbonnement);
        updatedAbonnement
            .dateAbonnement(UPDATED_DATE_ABONNEMENT)
            .type(UPDATED_TYPE);
        AbonnementDTO abonnementDTO = abonnementMapper.toDto(updatedAbonnement);

        restAbonnementMockMvc.perform(put("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnementDTO)))
            .andExpect(status().isOk());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
        Abonnement testAbonnement = abonnementList.get(abonnementList.size() - 1);
        assertThat(testAbonnement.getDateAbonnement()).isEqualTo(UPDATED_DATE_ABONNEMENT);
        assertThat(testAbonnement.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAbonnement() throws Exception {
        int databaseSizeBeforeUpdate = abonnementRepository.findAll().size();

        // Create the Abonnement
        AbonnementDTO abonnementDTO = abonnementMapper.toDto(abonnement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonnementMockMvc.perform(put("/api/abonnements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(abonnementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Abonnement in the database
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAbonnement() throws Exception {
        // Initialize the database
        abonnementRepository.saveAndFlush(abonnement);

        int databaseSizeBeforeDelete = abonnementRepository.findAll().size();

        // Delete the abonnement
        restAbonnementMockMvc.perform(delete("/api/abonnements/{id}", abonnement.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Abonnement> abonnementList = abonnementRepository.findAll();
        assertThat(abonnementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
