package com.openculture.org.web.rest;

import com.openculture.org.OpencultureApp;
import com.openculture.org.domain.Regroupement;
import com.openculture.org.repository.RegroupementRepository;
import com.openculture.org.service.RegroupementService;
import com.openculture.org.service.dto.RegroupementDTO;
import com.openculture.org.service.mapper.RegroupementMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RegroupementResource} REST controller.
 */
@SpringBootTest(classes = OpencultureApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RegroupementResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    @Autowired
    private RegroupementRepository regroupementRepository;

    @Autowired
    private RegroupementMapper regroupementMapper;

    @Autowired
    private RegroupementService regroupementService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegroupementMockMvc;

    private Regroupement regroupement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regroupement createEntity(EntityManager em) {
        Regroupement regroupement = new Regroupement()
            .type(DEFAULT_TYPE)
            .intitule(DEFAULT_INTITULE);
        return regroupement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regroupement createUpdatedEntity(EntityManager em) {
        Regroupement regroupement = new Regroupement()
            .type(UPDATED_TYPE)
            .intitule(UPDATED_INTITULE);
        return regroupement;
    }

    @BeforeEach
    public void initTest() {
        regroupement = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegroupement() throws Exception {
        int databaseSizeBeforeCreate = regroupementRepository.findAll().size();
        // Create the Regroupement
        RegroupementDTO regroupementDTO = regroupementMapper.toDto(regroupement);
        restRegroupementMockMvc.perform(post("/api/regroupements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regroupementDTO)))
            .andExpect(status().isCreated());

        // Validate the Regroupement in the database
        List<Regroupement> regroupementList = regroupementRepository.findAll();
        assertThat(regroupementList).hasSize(databaseSizeBeforeCreate + 1);
        Regroupement testRegroupement = regroupementList.get(regroupementList.size() - 1);
        assertThat(testRegroupement.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRegroupement.getIntitule()).isEqualTo(DEFAULT_INTITULE);
    }

    @Test
    @Transactional
    public void createRegroupementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regroupementRepository.findAll().size();

        // Create the Regroupement with an existing ID
        regroupement.setId(1L);
        RegroupementDTO regroupementDTO = regroupementMapper.toDto(regroupement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegroupementMockMvc.perform(post("/api/regroupements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regroupementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regroupement in the database
        List<Regroupement> regroupementList = regroupementRepository.findAll();
        assertThat(regroupementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRegroupements() throws Exception {
        // Initialize the database
        regroupementRepository.saveAndFlush(regroupement);

        // Get all the regroupementList
        restRegroupementMockMvc.perform(get("/api/regroupements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regroupement.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)));
    }
    
    @Test
    @Transactional
    public void getRegroupement() throws Exception {
        // Initialize the database
        regroupementRepository.saveAndFlush(regroupement);

        // Get the regroupement
        restRegroupementMockMvc.perform(get("/api/regroupements/{id}", regroupement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regroupement.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE));
    }
    @Test
    @Transactional
    public void getNonExistingRegroupement() throws Exception {
        // Get the regroupement
        restRegroupementMockMvc.perform(get("/api/regroupements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegroupement() throws Exception {
        // Initialize the database
        regroupementRepository.saveAndFlush(regroupement);

        int databaseSizeBeforeUpdate = regroupementRepository.findAll().size();

        // Update the regroupement
        Regroupement updatedRegroupement = regroupementRepository.findById(regroupement.getId()).get();
        // Disconnect from session so that the updates on updatedRegroupement are not directly saved in db
        em.detach(updatedRegroupement);
        updatedRegroupement
            .type(UPDATED_TYPE)
            .intitule(UPDATED_INTITULE);
        RegroupementDTO regroupementDTO = regroupementMapper.toDto(updatedRegroupement);

        restRegroupementMockMvc.perform(put("/api/regroupements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regroupementDTO)))
            .andExpect(status().isOk());

        // Validate the Regroupement in the database
        List<Regroupement> regroupementList = regroupementRepository.findAll();
        assertThat(regroupementList).hasSize(databaseSizeBeforeUpdate);
        Regroupement testRegroupement = regroupementList.get(regroupementList.size() - 1);
        assertThat(testRegroupement.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRegroupement.getIntitule()).isEqualTo(UPDATED_INTITULE);
    }

    @Test
    @Transactional
    public void updateNonExistingRegroupement() throws Exception {
        int databaseSizeBeforeUpdate = regroupementRepository.findAll().size();

        // Create the Regroupement
        RegroupementDTO regroupementDTO = regroupementMapper.toDto(regroupement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegroupementMockMvc.perform(put("/api/regroupements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regroupementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regroupement in the database
        List<Regroupement> regroupementList = regroupementRepository.findAll();
        assertThat(regroupementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegroupement() throws Exception {
        // Initialize the database
        regroupementRepository.saveAndFlush(regroupement);

        int databaseSizeBeforeDelete = regroupementRepository.findAll().size();

        // Delete the regroupement
        restRegroupementMockMvc.perform(delete("/api/regroupements/{id}", regroupement.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Regroupement> regroupementList = regroupementRepository.findAll();
        assertThat(regroupementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
